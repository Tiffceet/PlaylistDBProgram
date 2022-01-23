package looz.dreemur.generic;

import java.io.File;

/**
 *
 * @author Looz
 */
public class Util {

    /**
     * Get file path of a File object excluding the filename itself
     *
     * @param f The file object
     * @return absolute file path without filename
     */
    public static String getFilePathExcludeFile(File f) {
        String name = f.getName();
        String path = f.getAbsolutePath();

        // Construct path without file name.
        String pathWithoutFileName = path.substring(0, path.length() - name.length());
        if (pathWithoutFileName.endsWith("/")) {
            pathWithoutFileName = pathWithoutFileName.substring(0, pathWithoutFileName.length() - 1);
        }
        return pathWithoutFileName;
    }
}
