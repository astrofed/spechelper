package edu.spectrum.utils

import java.io.File

class FilenameFilter(var ext: String = null) extends java.io.FilenameFilter {

  def accept(dir: File, name: String): Boolean = name.toLowerCase.endsWith(ext)

}