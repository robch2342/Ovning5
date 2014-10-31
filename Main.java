import java.io.File;
import java.util.HashMap;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Välkommen till Svensk-Engelskt Lexikon 0.23");
        HashMap<String, String> lexicon;
        //Skapa sökvägen till lexikon.txt. System.getProperty("usr.dir") returnerar den katalog som programmet ligger i när det körs. I mitt fall blir detta "C:\Users\robin\IdeaProjects\Lexikon".
        String fileName = System.getProperty("user.dir");
        //Nu saknas en katalogseparator(\ i windows, eller / i *nix) och filnamnet. File.separator ger det tecken som systemet använder, så vårat program funkar på alla OS. "lexikon.txt" är namnet på filen vi vill öppna.
        //Egentligen hade det räckt att skriva fileName = "lexikon.txt", så antar File() att den ligger i samma katalog som programmet, men det visste jag inte när jag skrev det här.
        fileName += File.separator + "lexikon.txt";
        //Jag la själva inläsningen av filen i en annan funktion. LoadLexicon() läser in filen och returnerar lexikonet som en HashMap.
        lexicon = loadLexicon(fileName);
        //Om något gick fel vid inläsningen returnerar loadLexicon() null istf en HashMap. Om lexicon == null så kör jag return, vilket avslutar programmet.
        if (lexicon == null) {
            return;
        } else {
            //Om lexicon inte vsr null så körs funktionen searchLexicon() som låter användaren skriva in ord och söker efter dem i ordlistan.
            searchLexicon(lexicon);
        }
        //Säg hej då innan programmet avslutas.
        System.out.println("\nBye bye!");
    }

    private static HashMap<String, String> loadLexicon(String fileName) {
        //Skapa en ny HashMap att spara ordlistan i.
        HashMap<String, String> lexicon = new HashMap<String, String>();
        //try är en del av felhanteringen i java. Om något går fel inuti try-satsen så kommer programmet inte att krascha. I stället så avbryter den try och hoppar ner till catch.
        //Mycket kan gå fel vid inläsning av filer, så vi måste använda try här. Om vi inte gör det vägrar programmet kompilera, eftersom det vet att File() kan generera ett fel.
        try {
            //Skapa en ny File som läser in filen vars sökväg vi lagrat i strängen fileName. File läser in filen och genererar en dataström av den.
            File file = new File(fileName);
            //Scanner kan söka igenom en datström och plocka ut nästa ord eller rad. Vi har tidigare använt Scanner för att läsa in input via terminalen. Då sa vi åt Scanner att läsa
            //från System.in. System.in är också en dataström. Till skillnad från File läser den in det som användaren skriver i terminalen, men sen gör den ungefär samma sak med datan.
            Scanner words = new Scanner(file);
            //Skapa två strängar. En för att lagra det svenska ordet, och en för att lagra det engelska.
            String sWord, eWord;
            //words är våran Scanner. Funktionen hasNext() kollar om det finns ett till ord i dataströmmen som scannern läser från. Följande while-loop kommer bara köras så länge det
            //finns ett till ord att läsa in. När strömmen är slut kommer loopen att avslutas.
            while(words.hasNext()){
                //Sätt sWord till nästa ord i ordlistan. Varannat ord är ett svenskt ord, så nästa gång vi kör next() kommer vi få ett engelskt.
                sWord = words.next();
                //Nu läser vi in det engelska ordet i eWord.
                eWord = words.next();
                //Dags att lägga till orden i lexikonet. Funktionen toLowerCase() gör om alla versaler i strängen till gemener.
                lexicon.put(sWord.toLowerCase(), eWord);
            }
            //Nu är loopen slut. Vi avslutar med att stänga filen. Här stänger jag min Scanner. Den skickar vidare .close() till strömmen den läser från, så det funkar att göra så här.
            //OBS! Försök INTE göra detta om din Scanner läser från System.in. Den kommer att stänga System.in, vilket kommer att få programmet att krascha nästa gång du försöker läsa
            //System.in.
            words.close();
        //Här tar try slut. Det som händer när det uppstår ett fel är att det genereras ett objekt av klassen Exception. Detta objekt "kastas" sedan tillbaka. catch "fångar" dessa exceptions
        //och låter oss tala om vad som ska hända när ett fel uppstår. Om inget fel uppstår i try kommer catch aldrig att köras.
        } catch (Exception e) {
            //Något gick fel vid inläsningen. Sätt lexicon till null.
            lexicon = null;
            System.out.println("\nHittade inte lexikonet. Kontrollera om " + fileName + " existerar.\n");
            //Skriv ut lite information om vad som gick fel. e är den variabel vi lagrade vårat Exception i.
            System.out.println(e.getStackTrace());
        }
        //Returnera lexicon. Förhoppningsvis innehåller den ordlistan. Om ett fel uppstod är den null.
        return lexicon;
    }

    private static void searchLexicon(HashMap<String, String> lexicon){
        //Skapa en Scanner som läser från System.in så att vi kan läsa vad användaren skriver in i terminalen.
        Scanner input = new Scanner(System.in);
        //Skapa strängar att lagra översättning och input i.
        String translation, cmd;
        //Detta är en oändlig loop. Glöm inte att lägga till en funktion för att avbryta den inne i loopen.
        while (true){
            System.out.print("> Skriv in ett svenskt ord: ");
            //Lagra nästa rad användaren skriver in i strängen cmd.
            cmd = input.nextLine();
            //Kolla om cmd är "exit". Jag använder toLowerCase för att göra alla stora bokstäver små, så man kan skriva t.ex. Exit, eller EXIT för att avsluta.
            if (cmd.toLowerCase().equals("exit")){
                //Användaren skrev in exit, så programmet ska avslutas. return avbryter den här funktionen. Jag vet att funktionen main() kommer avslutas efter
                // att den här funktionen kört färdigt, så det funkar i detta fall. Eftersom inget händer efter while-loopen så hade det även funkat att använda
                //break, som bryter loopen.
                return;
            //Användaren skrev inte in "exit". Om användaren inte skrev in en tom rad (""), slå upp ordet i ordlistan.
            } else if (!cmd.equals("")){
                //Slå upp ordet i ordlistan. Använd toLowerCase så att vi ignorerar stora bokstäver. Om ordet inte finns så kommer get() returnera null.
                translation = lexicon.get(cmd.toLowerCase());
                //Om translation är null så fanns inte ordet i ordlistan, så då skriver vi det i terminalen.
                if (translation == null){
                    System.out.println("> " + cmd + " finns inte i mitt lexikon.");
                //Om translation inte är null så hittade vi en översättning. Skriv ut den på skärmen.
                } else {
                    System.out.println(cmd + " - " + translation);
                }
            }
        }
    }

}
