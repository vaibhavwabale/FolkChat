package in.icomputercoding.folkchat.Utils;



import java.io.File;
import java.util.ArrayList;


public class FileSearch {


    public static ArrayList<String> getDirectoryPaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listFiles = file.listFiles();
        for(int i = 0; i < listFiles.length; i++){
            if(listFiles[i].isDirectory()){
                pathArray.add(listFiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }


    public static ArrayList<String> getFilePaths(String directory){
        ArrayList<String> pathArray = new ArrayList<>();
        File file = new File(directory);
        File[] listfiles = file.listFiles();
        for(int i = 0; i < listfiles.length; i++){
            if(listfiles[i].isFile()){
                pathArray.add(listfiles[i].getAbsolutePath());
            }
        }
        return pathArray;
    }
}
