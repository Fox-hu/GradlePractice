package com.fox.router.gradle

import java.util.jar.JarEntry
import java.util.jar.JarFile

class RouterMappingCollector {

    private static final String PACKAGE_NAME = 'com/fox/gradlepractice/mapping'
    private static final String CLASS_NAME_PREFIX = 'RouterMapping_'
    private static final String CLASS_FILE_SUFFIX = '.class'

    private final Set<String> mappingClassNames = new HashSet<>()

    //获取收集到的映射表类名
    Set<String> getMappingClassName() {
        return mappingClassNames
    }

    //收集class文件或者class文件目录中的映射表类.
    void collect(File classFile) {
        if (classFile == null || !classFile.exists()) return
        if (classFile.isFile()) {
            if (classFile.absolutePath.contains(PACKAGE_NAME)
                    && classFile.name.startsWith(CLASS_NAME_PREFIX)
                    && classFile.name.endsWith(CLASS_FILE_SUFFIX)) {
                String className = classFile.name.replace(CLASS_FILE_SUFFIX, "")
                mappingClassNames.add(className)
            }
        } else {
            classFile.listFiles().each {
                collect(it)
            }
        }
    }

    //收集jar包中的目标类
    void collectFromJarFile(File jarFile) {
        Enumeration enumeration = new JarFile(jarFile).entries()
        while (enumeration.hasMoreElements()) {
            JarEntry jarEntry = (JarEntry) enumeration.nextElement()
            String entryName = jarEntry.getName()
            if (entryName.contains(PACKAGE_NAME)
                    && entryName.contains(CLASS_NAME_PREFIX)
                    && entryName.contains(CLASS_FILE_SUFFIX)) {
                String className = entryName
                        .replace(PACKAGE_NAME, "")
                        .replace("/", "")
                        .replace(CLASS_FILE_SUFFIX, "")
                mappingClassNames.add(className)
            }
        }
    }
}