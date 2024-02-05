public class MovieCollectionRunner
{
    public static void main(String arg[])
    {
        MovieCollection myCollection = new MovieCollection("src/movies_data");
        myCollection.menu();
    }
}