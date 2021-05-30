package fs;

import fs.Exceptions.CanNotOpenFileException;

public class HighLevelFileSystem {
    private LowLevelFileSystem lowLevelFileSystem;

    public HighLevelFileSystem(LowLevelFileSystem lowLevelFileSystem) {
        this.lowLevelFileSystem = lowLevelFileSystem;
    }

    public File open(String path){
        int fd = lowLevelFileSystem.openFile(path);
        if(fd <= 1){
            throw new CanNotOpenFileException("algo malo paso mientras abria el file");
        }
        return new File(fd,this.lowLevelFileSystem);
    }
}
