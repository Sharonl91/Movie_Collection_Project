import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.List;
public class MovieCollection
{
    private ArrayList<Movie> movies;
    private Scanner scanner;
    public MovieCollection(String fileName)
    {
        importMovieList(fileName);
        scanner = new Scanner(System.in);
    }
    public ArrayList<Movie> getMovies()
    {
        return movies;
    }
    public void menu()
    {
        String menuOption = "";
        System.out.println("Welcome to the movie collection!");
        System.out.println("Total: " + movies.size() + " movies");
        while (!menuOption.equals("q"))
        {
            System.out.println("------------ Main Menu ----------");
            System.out.println("- search (t)itles");
            System.out.println("- search (k)eywords");
            System.out.println("- search (c)ast");
            System.out.println("- see all movies of a (g)enre");
            System.out.println("- list top 50 (r)ated movies");
            System.out.println("- list top 50 (h)igest revenue movies");
            System.out.println("- (q)uit");
            System.out.print("Enter choice: ");
            menuOption = scanner.nextLine();
            if (!menuOption.equals("q"))
            {
                processOption(menuOption);
            }
        }
    }
    private void processOption(String option)
    {
        if (option.equals("t"))
        {
            searchTitles();
        }
        else if (option.equals("c"))
        {
            searchCast();
        }
        else if (option.equals("k"))
        {
            searchKeywords();
        }
        else if (option.equals("g"))
        {
            listGenres();
        }
        else if (option.equals("r"))
        {
            listHighestRated();
        }
        else if (option.equals("h"))
        {
            listHighestRevenue();
        }
        else
        {
            System.out.println("Invalid choice!");
        }
    }
    private void searchTitles()
    {
        System.out.print("Enter a title search term: ");
        String searchTerm = scanner.nextLine();
        searchTerm = searchTerm.toLowerCase();
        ArrayList<Movie> results = new ArrayList<Movie>();
        for (int i = 0; i < movies.size(); i++)
        {
            String movieTitle = movies.get(i).getTitle();
            movieTitle = movieTitle.toLowerCase();
            if (movieTitle.indexOf(searchTerm) != -1)
            {
                results.add(movies.get(i));
            }
        }
        sortResults(results);
        for (int i = 0; i < results.size(); i++)
        {
            String title = results.get(i).getTitle();
            int choiceNum = i + 1;
            System.out.println("" + choiceNum + ". " + title);
        }
        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        Movie selectedMovie = results.get(choice - 1);
        displayMovieInfo(selectedMovie);
        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }
    private void sortResults(ArrayList<Movie> listToSort)
    {
        for (int j = 1; j < listToSort.size(); j++)
        {
            Movie temp = listToSort.get(j);
            String tempTitle = temp.getTitle();
            int possibleIndex = j;
            while (possibleIndex > 0 && tempTitle.compareTo(listToSort.get(possibleIndex - 1).getTitle()) < 0)
            {
                listToSort.set(possibleIndex, listToSort.get(possibleIndex - 1));
                possibleIndex--;
            }
            listToSort.set(possibleIndex, temp);
        }
    }
    private void displayMovieInfo(Movie movie)
    {
        System.out.println();
        System.out.println("Title: " + movie.getTitle());
        System.out.println("Tagline: " + movie.getTagline());
        System.out.println("Runtime: " + movie.getRuntime() + " minutes");
        System.out.println("Year: " + movie.getYear());
        System.out.println("Directed by: " + movie.getDirector());
        System.out.println("Cast: " + movie.getCast());
        System.out.println("Overview: " + movie.getOverview());
        System.out.println("User rating: " + movie.getUserRating());
        System.out.println("Box office revenue: " + movie.getRevenue());
    }
    private void searchCast()
    {
        System.out.print("Enter a cast search term: ");
        String searchTerm = scanner.nextLine().toLowerCase();
        ArrayList<String> castList = new ArrayList<>();
        for (Movie movie : movies) {
            String[] castMembers = movie.getCast().split("\\|");
            for (String castMember : castMembers) {
                String cleanedCastMember = castMember.trim().toLowerCase();
                if (!castList.contains(cleanedCastMember)) {
                    castList.add(cleanedCastMember);
                }
            }
        }
        castList.sort(String::compareToIgnoreCase);
        ArrayList<String> searchResults = new ArrayList<>();
        for (String castMember : castList) {
            if (castMember.contains(searchTerm)) {
                searchResults.add(castMember);
            }
        }
        if (searchResults.isEmpty()) {
            System.out.println("No matching cast members found.");
            return;
        }
        System.out.println("Matching cast members:");
        for (int i = 0; i < searchResults.size(); i++) {
            System.out.println((i + 1) + ". " + searchResults.get(i));
        }
        System.out.print("Enter the number of the cast member to see movies: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > searchResults.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        String selectedCastMember = searchResults.get(choice - 1);
        ArrayList<Movie> moviesForCastMember = new ArrayList<>();
        for (Movie movie : movies) {
            String[] castMembers = movie.getCast().split("\\|");
            for (String castMember : castMembers) {
                if (castMember.trim().equalsIgnoreCase(selectedCastMember)) {
                    moviesForCastMember.add(movie);
                    break;
                }
            }
            sortResults(moviesForCastMember);
        }
        System.out.println("Movies for " + selectedCastMember + ":");
        for (int i = 0; i < moviesForCastMember.size(); i++) {
            System.out.println((i + 1) + ". " + moviesForCastMember.get(i).getTitle());
        }
        System.out.print("Enter the number of the movie to learn more: ");
        choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > moviesForCastMember.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        Movie selectedMovie = moviesForCastMember.get(choice - 1);
        displayMovieInfo(selectedMovie);
        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }
    private void searchKeywords()
    {
        System.out.print("Enter a keyword search term: ");
        String searchTerm = scanner.nextLine();
        searchTerm = searchTerm.toLowerCase();
        ArrayList<Movie> results = new ArrayList<Movie>();
        for (int i = 0; i < movies.size(); i++)
        {
            String movieKeyword = movies.get(i).getKeywords();
            movieKeyword = movieKeyword.toLowerCase();
            if (movieKeyword.indexOf(searchTerm) != -1)
            {
                results.add(movies.get(i));
            }
        }
        sortResults(results);
        for (int i = 0; i < results.size(); i++)
        {
            String keyword = results.get(i).getKeywords();
            int choiceNum = i + 1;
            System.out.println("" + choiceNum + ". " + keyword);
        }
        System.out.println("Which movie would you like to learn more about?");
        System.out.print("Enter number: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        Movie selectedMovie = results.get(choice - 1);
        displayMovieInfo(selectedMovie);
        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }
    private void listGenres()
    {
        ArrayList<String> genreList = new ArrayList<>();
        for (Movie movie : movies) {
            String[] genres = movie.getGenres().split("\\|");
            for (String genre : genres) {
                String cleanedGenre = genre.trim().toLowerCase();
                if (!genreList.contains(cleanedGenre)) {
                    genreList.add(cleanedGenre);
                }
            }
        }
        genreList.sort(String::compareToIgnoreCase);
        System.out.println("Enter choice: ");
        for (int i = 0; i < genreList.size(); i++) {
            System.out.println((i + 1) + ". " + genreList.get(i));
        }
        System.out.print("Enter the number of the genre to see movies: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > genreList.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        String selectedGenre = genreList.get(choice - 1);
        ArrayList<Movie> moviesForGenre = new ArrayList<>();
        for (Movie movie : movies) {
            String[] genres = movie.getGenres().split("\\|");
            for (String genre : genres) {
                if (genre.trim().equalsIgnoreCase(selectedGenre)) {
                    moviesForGenre.add(movie);
                    break;
                }
            }
        }
        sortResults(moviesForGenre);
        System.out.println("Movies in " + selectedGenre + " genre:");
        for (int i = 0; i < moviesForGenre.size(); i++) {
            System.out.println((i + 1) + ". " + moviesForGenre.get(i).getTitle());
        }
        System.out.print("Enter the number of the movie to learn more: ");
        choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > moviesForGenre.size()) {
            System.out.println("Invalid choice.");
            return;
        }
        Movie selectedMovie = moviesForGenre.get(choice - 1);
        displayMovieInfo(selectedMovie);
        System.out.println("\n ** Press Enter to Return to Main Menu **");
        scanner.nextLine();
    }
    private void listHighestRated() {
        ArrayList<Movie> sortedMovies = new ArrayList<>(movies);
        for (int i = 0; i < sortedMovies.size() - 1; i++) {
            for (int j = 0; j < sortedMovies.size() - i - 1; j++) {
                if (sortedMovies.get(j).getUserRating() < sortedMovies.get(j + 1).getUserRating()) {
                    Movie temp = sortedMovies.get(j);
                    sortedMovies.set(j, sortedMovies.get(j + 1));
                    sortedMovies.set(j + 1, temp);
                }
            }
        }
        int limit = Math.min(50, sortedMovies.size());
        for (int i = 0; i < limit; i++) {
            System.out.println((i + 1) + ". " + sortedMovies.get(i).getTitle() +
                    " (Rating: " + sortedMovies.get(i).getUserRating() + ")");
        }
        System.out.print("Enter the number of the movie to learn more: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > limit) {
            System.out.println("Invalid choice.");
            return;
        }
        Movie selectedMovie = sortedMovies.get(choice - 1);
        displayMovieInfo(selectedMovie);
    }
    private void listHighestRevenue()
    {
        ArrayList<Movie> sortedMovies = new ArrayList<>(movies);
        int limit = Math.min(50, sortedMovies.size());
        for (int i = 0; i < limit; i++) {
            System.out.println((i + 1) + ". " + sortedMovies.get(i).getTitle() +
                    " (Revenue: $" + sortedMovies.get(i).getRevenue() + ")");
        }
        System.out.print("Enter the number of the movie to learn more: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        if (choice < 1 || choice > limit) {
            System.out.println("Invalid choice.");
            return;
        }
        Movie selectedMovie = sortedMovies.get(choice - 1);
        displayMovieInfo(selectedMovie);
    }
    private void importMovieList(String fileName)
    {
        try
        {
            FileReader fileReader = new FileReader(fileName);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line = bufferedReader.readLine();
            movies = new ArrayList<Movie>();
            while ((line = bufferedReader.readLine()) != null)
            {
                String[] movieFromCSV = line.split(",");
                String title = movieFromCSV[0];
                String cast = movieFromCSV[1];
                String director = movieFromCSV[2];
                String tagline = movieFromCSV[3];
                String keywords = movieFromCSV[4];
                String overview = movieFromCSV[5];
                int runtime = Integer.parseInt(movieFromCSV[6]);
                String genres = movieFromCSV[7];
                double userRating = Double.parseDouble(movieFromCSV[8]);
                int year = Integer.parseInt(movieFromCSV[9]);
                int revenue = Integer.parseInt(movieFromCSV[10]);
                Movie nextMovie = new Movie(title, cast, director, tagline, keywords, overview, runtime, genres, userRating, year, revenue);
                movies.add(nextMovie);
            }
            bufferedReader.close();
        }
        catch(IOException exception)
        {
            System.out.println("Unable to access " + exception.getMessage());
        }
    }
}