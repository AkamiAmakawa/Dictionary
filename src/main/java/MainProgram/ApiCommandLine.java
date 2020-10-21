package MainProgram;

import com.darkprograms.speech.synthesiser.SynthesiserV2;
import com.darkprograms.speech.translator.GoogleTranslate;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import java.io.IOException;

/**
 * Shout out to goxr3plus for lending us API's key and this code.
 * Thank you so much, Soviet's comrades.
 */

public class ApiCommandLine {

    public static void Speak(String text) {
        //Create a new Thread because JLayer is running on the current Thread and will make the application to lag
        Thread thread = new Thread(() -> {
            try {

                //Create a JLayer instance
                final String apiKey = "AIzaSyBOti4mM-6x9WDnZIjIeyEU21OpBXqWBgw";
                SynthesiserV2 synthesizer = new SynthesiserV2(apiKey);
                AdvancedPlayer player = new AdvancedPlayer(synthesizer.getMP3Data(text));
                player.play();


            } catch (IOException | JavaLayerException e) {
                System.out.println("Unable to get synthesizer data");
                e.printStackTrace(); //Print the exception ( we want to know , not hide below our finger , like many developers do...)

            }
        });

        //We don't want the application to terminate before this Thread terminates
        thread.setDaemon(false);

        //Start the Thread
        thread.start();
    }

    public static String onlineTranslator(String text) {
        String result = "";
        try {
            result = GoogleTranslate.translate("vi", text);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
}
