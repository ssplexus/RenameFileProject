import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Main
{
    public static void main(String[] args) throws IOException {
        // Проверка аргументов
        if(args.length < 2)
        {
            System.out.println("Not enough arguments! Need 2 arguments");
            return;
        }
        else if (args.length > 2)
            {
                System.out.println("Too much arguments! Need 2 arguments");
                return;
            }
        File file = new File(args[0]);
        if(!file.isDirectory())
        {
            System.out.println("Such dir not exists!");
            return;
        }
        renFilesInDir(file, args[1]);
    }

    /** Метод для переименования файлов указанного расширения в datastamp их параметра lastModified
     *
     * @param folder - рабочая папка
     * @param ext - расширение файлов для переименования
     * @throws IOException
     */
    public static void renFilesInDir(File folder, String ext) throws IOException {
        double size = 0;

        for(File f: folder.listFiles()) // Обход списка файлов в папке и подпаках
        {
            if (f.isDirectory()) renFilesInDir(f, ext); // если папка то вызываем этот метод для неё
            else
            {
                size = Math.max(f.length(), size); // находим максимальный размер файла в папке

                int lastIndexOf = f.getName().lastIndexOf(".");
                if (lastIndexOf > 0)
                {
                    if( ext.equals(f.getName().substring(lastIndexOf + 1))) // определение файла с нужным расширением
                    {
                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy_HHmmss"); // формируем datastamp

                        // Переименование файла
                        if(!f.renameTo(new File(folder.getAbsolutePath() + "\\" + dateFormat.format(f.lastModified()) + "." + ext)))
                            System.out.println(String.format("File %s cannot be renamed", f.getName()));
                    }
                }
            }
        }
        // подбор единицы измерения размера файла для вывода
        String unit = "B";
        if(size  > 1024)
        {
            if(size > 1024 * 1024)
            {
                size = size / 1024 / 1024;
                unit = "MB";
            }
            else
            {
                size /= 1024;
                unit = "KB";
            }
        }

        // Создание txt файла содержащего в своём имени макисмальный размер файла в папке
        File file = new File(folder.getAbsolutePath() + "\\" + Math.round(size) + unit + ".txt");
        if(!file.exists())
        {
            if(file.createNewFile())
                System.out.println(file.getAbsolutePath() + " is created!");
        }
        else
            System.out.println("File already exists!");
    }
}
