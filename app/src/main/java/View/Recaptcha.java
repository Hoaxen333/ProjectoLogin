package View;

import java.util.Random;

public class Recaptcha {
    private String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
    private StringBuffer stringBuffer;
    private static final int TAMANHO = 6;
    private Random random;

    Recaptcha(){
        this.random = new Random();
        this.stringBuffer = new StringBuffer();
    }
    StringBuffer recaptchaCode(){
        while(stringBuffer.length()<TAMANHO){
            int aleatorio = random.nextInt(caracteres.length()-1);
            stringBuffer.append(caracteres.charAt(aleatorio));
        }
        return stringBuffer;
    }
    public static void main(String[] args){
        Recaptcha r = new Recaptcha();
        System.out.println(r.recaptchaCode());
    }
}
