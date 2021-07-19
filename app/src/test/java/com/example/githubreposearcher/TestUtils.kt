package com.example.githubreposearcher.common

import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

/**
 * Helper method to load a file from test/resources folder and parse it to string.
 */
fun loadTestResource(resourceName: String): String {
    val path = Paths.get(ClassLoader.getSystemClassLoader().getResource(resourceName).toURI())
    return String(Files.readAllBytes(path), Charset.forName("UTF8"))
}