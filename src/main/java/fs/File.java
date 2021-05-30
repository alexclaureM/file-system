package fs;

import fs.Exceptions.CanNotReadFileException;
import fs.Exceptions.ReadFileException;

import java.util.function.Consumer;

public class File {
    private int fd;
    private LowLevelFileSystem lowLevelFileSystem;

    public File(int fd, LowLevelFileSystem lowLevelFileSystem) {
        this.fd = fd;
        this.lowLevelFileSystem = lowLevelFileSystem;
    }
    public void closed(){
        lowLevelFileSystem.closeFile(fd);
    }
    //lectura sincronica
    public void read(Buffer buffer){
        int readBytes = lowLevelFileSystem.syncReadFile(fd,
                buffer.getBytes(),
                buffer.getStart(),
                buffer.getEnd());
        if(readBytes <= -1){
            throw new CanNotReadFileException("Archivo no existe");
        }
        buffer.limit(readBytes);
    }
    // escritura sincrinica
    public void write(Buffer buffer){
        lowLevelFileSystem.syncWriteFile(fd,buffer.getBytes(),buffer.getStart(), buffer.getEnd());
    }

    //lectura asincronica -- recorda que solo la lectura es asincronica, para que quiero escribir asincronicamente??
    public void asyncRead(Consumer<Buffer> callback){
        Buffer buffer = new Buffer(100);
        lowLevelFileSystem.asyncReadFile(fd,
                buffer.getBytes(),
                buffer.getStart(),
                buffer.end,
                readBytes -> {
                        buffer.limit(readBytes);
                        callback.accept(buffer);
                });
    }

    public int getDescriptor() {
        return fd;
    }

}
