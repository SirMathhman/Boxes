package com.meti.box;

import com.meti.util.Checker;
import com.meti.util.Clause;

import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static com.meti.util.Clause.*;

/**
 * @author SirMathhman
 * @version 0.0.0
 * @since 1/17/2019
 */
public class JARBoxBuilder implements BoxBuilder<Path> {
    @Override
    public Box build(Path source) throws Exception {
        checkJar(source);

        ZipFile jarZip = createZip(source);
        Set<String> classNames = getClassNames(jarZip);

        URLClassLoader classLoader = new URLClassLoader(new URL[]{source.toUri().toURL()});
        Class[] classes = classNames.stream()
                .map(wrap((Clause<String, Class<?>>) classLoader::loadClass))
                .flatMap(Optional::stream).distinct().toArray(Class[]::new);

        String fileName = source.getFileName().toString();
        Path folder = source.getParent().resolve(fileName.substring(0, fileName.indexOf('.')));

        Set<Box> subBoxes = new HashSet<>();
        if(Files.exists(folder)){
            Files.list(folder)
                    .filter(Checker.wrap(this::checkJar))
                    .map(Clause.wrap(this::build))
                    .flatMap(Optional::stream)
                    .forEach(subBoxes::add);
        }

        return new Box(subBoxes, classes);
    }

    void checkJar(Path source) {
        if (!Files.exists(source)) {
            throw new IllegalArgumentException(source.toString() + " does not exist!");
        }

        if (Files.isDirectory(source)) {
            throw new IllegalArgumentException("Cannot perform operation on directory " + source + "!");
        }

        /*remember, a jar is simply a runnable zip file!
        also, don't change this line to

        if (!source.endsWith(".jar"){
            ...
        }

        because apparently this code doesn't work due to the way that
        files are referred

        */
        if (!source.toString().endsWith(".jar")) {
            throw new IllegalArgumentException(source.toString() + " is not a jar!");
        }
    }

    ZipFile createZip(Path source) throws IOException {
        ZipFile jarZip = new ZipFile(source.toFile());
        if (jarZip.size() == 0) {
            jarZip.close();
            throw new IllegalArgumentException("Found an empty jar at " + source.toString() + "!");
        }
        return jarZip;
    }

    Set<String> getClassNames(ZipFile jarZip) {
        return Collections.list(jarZip.entries())
                .parallelStream()
                .filter((Predicate<ZipEntry>) zipEntry -> zipEntry.toString().endsWith(".class"))
                .map((Function<ZipEntry, String>) ZipEntry::toString)
                .map(s -> s.contains("/") ? s.substring(s.indexOf('/')) : s)
                .map(s -> s.substring(0, s.lastIndexOf('.')))
                .collect(Collectors.toSet());
    }
}
