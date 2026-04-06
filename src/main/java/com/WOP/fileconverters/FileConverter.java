package com.WOP.fileconverters;

import java.io.File;
import java.io.IOException;

public interface FileConverter {
  // All converters convert to csv
  File convert(File input) throws IOException;
}
