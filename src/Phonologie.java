
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

/**
 *
 * @author michael
 */
public class Phonologie {
    public static void main(String[] args) throws IOException {
        Random r = new Random(System.currentTimeMillis());
        
        
        String[] initials = {"1", "2", "2", "2", "12", "2J", "2", "2J", "3", "123", ""};
        String[] medials = {"45", "5", "J5", "45", "5"};
        String[] finals = {"6", "7", "8", "67", "6", "7", "8", "67", "78", "68", "678", "678", ""};
        
        String[] vowel = {"e", "e", "e", "e", "ø", "u", "ɑ"};
        String[] fricative = {"f", "s", "s", "s", "s"};
        String[] plosive = {"p", "pʰ", "pʷ", "b", "t", "tʰ", "tʷ", "d", "k", "kʰ", "kʷ", "g", "ʔ", "p", "b", "t", "d", "k", "g", "p", "b", "t", "d", "k", "g"};
        String[] nasal = {"m", "n", "ŋ"};
        String[] approximant = {"r", "j", "w", "ɫ", "h", "hʷ"};
        String[] fricative_nasal = {"f", "s", "m", "n", "ŋ"};
        String[] approximant_nasal = {"m", "n", "ŋ", "r", "j", "w", "h", "hʷ", "j", "w", "h", "hʷ", "ɫ"};
        
        shuffle(initials);
        shuffle(medials);
        shuffle(finals);
        shuffle(vowel);
        shuffle(fricative);
        shuffle(plosive);
        shuffle(nasal);
        shuffle(approximant);
        shuffle(fricative_nasal);
        shuffle(approximant_nasal);
        
        /*
        String[] initials = {"1", "12", "2", "2J", "3", ""};
        String[] medials = {"45", "5", "J5"};
        String[] finals = {"6", "7", "8", "67", "78", "68", "678", ""};
        
        String[] vowel = {"e", "ø", "u", "ɑ"};
        String[] fricative = {"f", "s", "s", "", ""};
        String[] plosive = {"p", "pʰ", "pʷ", "b", "t", "tʰ", "tʷ", "d", "k", "kʰ", "kʷ", "g", "ʔ"};
        String[] nasal = {"m", "n", "ŋ"};
        String[] approximant = {"r", "j", "w", "ɫ", "h", "hʷ"};
        String[] fricative_nasal = {"f", "s", "m", "n", "ŋ", ""};
        String[] approximant_nasal = {"m", "n", "ŋ", "r", "j", "w", "h", "hʷ", "ɫ"};
        */
        
        String last = "";
        Set<String> keepers = new HashSet<>();
        Scanner scan = new Scanner(System.in);
        
        while (true) {
            String ln = scan.nextLine();
            char read = (ln.length() == 1 ? ln.charAt(0) : '\0');
            
            if (read == 'k')
                keepers.add(last);
            
            if (read == 'x') {
                System.out.print("\n\n\n---------------------------\n\n\n");
                
                for (String s : keepers)
                    System.out.println(s);
                
                break;
            }
            
            while (true) {
                String form1 = choose(initials, r) + choose(medials, r) + choose(finals, r);
                String form = choose(new String[] {form1}, r);
                StringBuilder sb = new StringBuilder();
                for (char c : form.toCharArray()) {
                    switch (c) {
                        case '1':
                            sb.append(choose(fricative, r));
                            break;
                        case '2':
                            sb.append(choose(plosive, r));
                            break;
                        case '3':
                            sb.append(choose(fricative_nasal, r));
                            break;
                        case '4':
                            sb.append(choose(approximant_nasal, r));
                            break;
                        case '5':
                            sb.append(choose(vowel, r));
                            break;
                        case '6':
                            sb.append(choose(approximant_nasal, r));
                            break;
                        case '7':
                            sb.append(choose(plosive, r));
                            break;
                        case '8':
                            sb.append(choose(fricative, r));
                            break;
                        case 'N':
                            sb.append(choose(nasal, r));
                            break;
                        case 'J':
                            sb.append(choose(approximant, r));
                            break;
                    }
                }
                
                String out = sb.toString();
                
                if ((out.length() <= 2 || out.length() > 4) && possiblyKeep(1.0/3, r))
                    continue;
                
                
                if ((out.matches("f.*f") || out.matches("s.*f")) && possiblyKeep(1.0/2, r)) {
                    report("skipped " + out + " for bad overfricative.");
                    continue;
                }
                
                if (out.matches(".*[nmŋ].?[nmŋ].*")) {
                    report("skipped " + out + " for oversymmetry.");
                    continue;
                }
                
                if (out.matches(".*[ɫrw].?[ɫrw].*")) {
                    report("skipped " + out + " for oversymmetry.");
                    continue;
                }
                
                if (out.matches(".*[wʷ].?.?[wʷ].*")) {
                    report("skipped " + out + " for over-labialization.");
                    continue;
                }
                
                if (!out.matches(".*[pbkgtd].*") && possiblyKeep(1.0/1.5, r)) {
                    report("skipped " + out + " for no stop.");
                    continue;
                }
                
                if (out.matches(".*f[pbkgtd].*") || out.matches(".*[pbkgtd]f.*")) {
                    report("skipped " + out + " for fP.");
                    continue;
                }
                
                if (out.matches(".*p.*p.*") ||
                    out.matches(".*b.*b.*") ||
                    out.matches(".*t.*t.*") ||
                    out.matches(".*d.*d.*") ||
                    out.matches(".*k.*k.*") ||
                    out.matches(".*g.*g.*")) {
                    report("skipped " + out + " for plosive harmony");
                    continue;
                }
                
                if (out.contains("jɑ") || out.contains("ɑj") || 
                    (out.contains("eh") && !out.contains("ehʷ")) || out.contains("he") || 
                    out.contains("uw") || out.contains("wu") ||
                    out.contains("hʷø") || out.contains("øhʷ")) {
                    report("skipped " + out + " for bad semivowels.");
                    continue;
                }
                
                
                if (out.replaceAll("hʷ", "H").matches(".*[jwhH][ɑeøu][jwhH].*")) {
                    System.out.print("*** ");
                }
                
                System.out.print(form+" -> "+out+" ");
                last = out;
                break;
            }
        }
    }

    private static String choose(String[] s, Random r) {
        double d = r.nextDouble();
            
        return s[(int)(d * s.length)];
    }

    private static boolean possiblyKeep(double d, Random r) {
        return r.nextDouble() > d;
    }

    private static void report(String string) {
        System.out.println(string);
    }

    private static void shuffle(String[] list) {
        Collections.shuffle(Arrays.asList(list));
    }
}
