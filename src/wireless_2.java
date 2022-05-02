import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class wireless_2 {

	public static int poisson(float lambda) {
		Random random = new Random();
		double L = Math.exp((double) (-lambda));
		double p = 1.0;
		int k = 0;

		while (p > L) {
			k++;
			float u = random.nextFloat();
			p = p * u;
		}
		return k - 1;
	}

	public static ArrayList corrupt(int poisson) {
		Random random = new Random();

		System.out.println("\nPoisson: " + poisson + "\n");
		ArrayList<Integer> list = new ArrayList();

		for (int i = 0; i < 79; i++) {
			list.add(i + 1);
		}

		for (int i = 0, j = 79; i < 39; i++, j--) { // possible corrupted channel: 79 - 39 = 40
			list.remove(random.nextInt(j));
		}
		System.out.println("Possible corrupted channel: \n" + list + "\n");

		for (int i = 0, j = 40; i < 40 - poisson; i++, j--) { // corrupted channel based on Poisson distribution
			list.remove(random.nextInt(j));
		}
		System.out.println("Actual corrupted channel: \n" + list + "\n");

		return list;
	}

	// --------------------------------------------------------------

	public static void main(String[] args) {
		Random random = new Random();
		ArrayList<Integer> list = new ArrayList();
		double ch_rate[] = new double[79];
		double ch_count[] = new double[79];
		int ch_bad[] = new int[79];

		for (int k = 40; k < 70; k += 10) {
			Arrays.fill(ch_count, 0.0);
			Arrays.fill(ch_bad, 0);

			for (int i = 0; i < 48000; i++) {
				Arrays.fill(ch_count, 0.0);

				for (int j = 0; j < k; j++) {
					ch_count[random.nextInt(79)]++;
				}

				for (int j = 0; j < 79; j++) {
					if (ch_count[j] > 1)
						ch_rate[j]++;
				}
			}

			System.out.print("#" + k + "\n");

			for (int i = 0; i < 79; i++) {
				System.out.printf("%f ", ch_rate[i] / 48000);
			}

			System.out.println();

			list = corrupt(poisson(20)); // list of corrupted channel

			for (double i = 0.1; i <= 0.9; i += 0.1) {
				int count = 0;
				Arrays.fill(ch_bad, 0);

				for (int l = 0; l < list.size(); l++) {
					ch_bad[(list.get(l) - 1)] = 1;
				}

				for (int j = 0; j < 79; j++) {
					if ((ch_rate[j] / 48000) > i) { // i = threshold
						// count++;
						ch_bad[j] = 1;
					}
				}

				for (int j = 0; j < 79; j++) {
					if (ch_bad[j] == 1)
						count++;
				}
				// System.out.print(count + ":\t");
				System.out.printf("(%.1f) %d:\t", i, count);

				for (int l = 0; l < ch_bad.length; l++) {
					System.out.print(ch_bad[l] + " ");
				}
				System.out.println();
			}

			System.out.println("\n");
			Arrays.fill(ch_rate, 0.0);
		}
	}
}
