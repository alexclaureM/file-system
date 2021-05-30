package fs.Exceptions;

public class CanNotReadFileException extends RuntimeException {
    public CanNotReadFileException(String archivo_no_existe) {
        super(archivo_no_existe);
    }
}
