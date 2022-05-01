import java.util.Random;

public class wireless_1 {

	public static void main(String[] args) {
		Random random = new Random();
		int a = 0, b = 0;
		double count = 0.0;

		for (int i = 0; i < 48000; i++) {
			a = random.nextInt(79) + 1;
			b = random.nextInt(79) + 1;

			if (a == b) {
				count++;
				System.out.printf("#%d\t%d\n", (int) count, a);
			}
		}		
		System.out.printf("\nCollision Probability = %.4f", (count / 48000.0));
	}
}
