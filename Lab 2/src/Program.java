import java.util.Scanner;

public class Program {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        RabinCryptoSystem crypto = new RabinCryptoSystem();
        crypto.printPublicKeys();
        System.out.println("Enter the message you want to encrypt and pair of public keys (n, b) in different lines");
        //System.out.println("Your encrypted message:\n" + crypto.encrypt(in.nextLine(), in.nextLine(), in.nextLine()));
        System.out.println("\nEnter the message you want to decrypt");
        String[] decrypted = crypto.decrypt(crypto.encryptByPublicKeys(in.nextLine()));
        //String[] decrypted = crypto.decrypt(in.nextLine());
        System.out.println("Your decrypted message in one of the next lines:");
        for(String m : decrypted)
            System.out.println(m);
    }
}
