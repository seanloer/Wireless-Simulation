import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class wireless_3 {

	static ArrayList<Integer> bad_channel = new ArrayList();
	static ArrayList<Integer> pos_seq = new ArrayList();

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
		pos_seq.add(k - 1);
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

		for (int i = 0, j = 40; i < 40 - poisson; i++, j--) { // corrupted channel based on Poisson
			list.remove(random.nextInt(j));
		}
		System.out.println("Actual corrupted channel: \n" + list + "\n");

		return list;
	}

	public static ArrayList sequence(int id) { // generating hopping sequence by device id												
		ArrayList<Integer> list = new ArrayList();

		Random random = new Random();
		random.setSeed(id);

		for (int i = 0; i < 79; i++) {
			list.add(random.nextInt(79) + 1);
		}
		return list;
	}

	public static int check(int tmp, int[] ch_bad) { // REMAPPING
		int channel = tmp;
		int right = tmp, left = tmp;

		for (int i = 0; i < 40; i++) { // whole channel (left + right)
			if ((i + 1) % 2 == 0)
				tmp = right;
			else
				tmp = left;

			// System.out.printf("T: %d\tL: %d\tR: %d\n", tmp, left, right);

			if (ch_bad[tmp] == 0) {
				// System.out.print(tmp);
				channel = tmp;
				break;
			} else {
				if ((right + i) > 78)
					right = (right + i) - 78;
				else
					right += i;

				if ((left - i) < 0)
					left = 78 + (left - i);
				else
					left -= i;
			}
		}
		return channel;
	}

	public static int[] part_2() {
		Random random = new Random();
		ArrayList<Integer> list = new ArrayList();
		double ch_rate[] = new double[79];
		double ch_count[] = new double[79];
		int ch_bad[] = new int[79];

		for (int k = 40; k < 70; k += 10) { // # of devices 40, 50, 60
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

			// 40 * 0.5 = 20
			list = corrupt(poisson(20)); // list of corrupted channel

			bad_channel.addAll(list);

			for (double i = 0.1; i <= 0.9; i += 0.1) {
				int count = 0;
				Arrays.fill(ch_bad, 0);

				for (int l = 0; l < list.size(); l++) {
					ch_bad[(list.get(l) - 1)] = 1;
				}

				for (int j = 0; j < 79; j++) { // i = threshould
					if ((ch_rate[j] / 48000) > i)
						ch_bad[j] = 1;
				}

				for (int j = 0; j < 79; j++) {
					if (ch_bad[j] == 1)
						count++;
				}

				System.out.printf("(%.1f) %d:\t", i, count);

				for (int l = 0; l < ch_bad.length; l++) {
					System.out.print(ch_bad[l] + " ");
				}
				System.out.println();
			}
			System.out.println("\n");
			Arrays.fill(ch_rate, 0.0);
		}
		System.out.println("\n");
		System.out.println("\n");
		return ch_bad;
	}

	// --------------------------------------------------------------

	public static void main(String[] args) {
		Random random = new Random();

		int ch_bad[] = part_2(); // record bad channel
		double count = 0.0;
		double ch_count[] = new double[79];
		double ch_rate[] = new double[79];
		
		for(int h = 0; h < 9; h++) {
			for (int k = 40; k < 70; k += 10) {
				Arrays.fill(ch_count, 0.0);
				Arrays.fill(ch_rate, 0.0);

				System.out.println("#" + k);
				count = 0.0;

				for (int i = 0; i < 48000; i++) {
					// count = 0.0;
					Arrays.fill(ch_count, 0.0);
					
					for (int j = 0; j < k; j++) {
						int temp = random.nextInt(79);
						System.out.print(temp);
						int a = check(temp, ch_bad);
						// System.out.println(a);
						ch_count[a]++;
					}

					for (int j = 0; j < 79; j++) {
						// if(ch_count[j] > 1) ch_rate[j]++;
						if (ch_count[j] > 1)
							count++;
					}
				}				
				System.out.printf("%.4f\n", count / (48000.0 * 79.0));
			}
			System.out.println("----------------");
		}
		System.out.println(bad_channel + "\n" + pos_seq);
	}
}
