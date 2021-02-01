import java.util.Scanner;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Comparator;

class Date implements Comparable<Date> {
	private int val;// 10000*year+100*month+day

	private static boolean leapYear(int y) {
		if (y % 400 == 0)
			return true;
		if (y % 100 == 0)
			return false;
		return (y & 3) == 0;// 101010011011101010101 & 0000011 = 01
							// y%4==0
	}

	Date(String s) {
		final HashSet<Integer> shortMonths = new HashSet<Integer>(Arrays.asList(2, 4, 6, 9, 11));
		val = 0;
		s += '.';
		int[] d = new int[3];
		for (int i = 0; i < 3; i++) {
			int p = s.indexOf('.');
			if (p < 0)
				return;
			try {
				d[i] = Integer.parseInt(s.substring(0, p));
			} catch (Exception e) {
				return;
			}
			s = s.substring(p + 1);
		}
		if (d[2] < 0)
			return;
		if (d[1] < 1 || d[1] > 12)
			return;
		if (d[0] < 1 || d[0] > 31)
			return;
		if (shortMonths.contains(d[1]) && d[0] > 30)
			return;
		if (d[1] == 2) {
			if (d[0] > 29)
				return;
			if (d[0] > 28 && !leapYear(d[2]))
				return;
		}
		val = d[0] + 100 * d[1] + 10000 * d[2];
	}

	@Override
	public int compareTo(Date d) throws IllegalStateException {
		if (val == 0)
			throw new IllegalStateException();
		return this.val - d.val;
	}

	int getYear() {
		return val / 10000;
	}

	@Override
	public String toString() {
		if (val == 0)
			return "Illegal date";
		return String.format("%02d.%02d.%04d", val % 100, (val / 100) % 100, val / 10000);
	}
}

class Client implements Comparable<Client> {
	private String name;
	private Date regDate;
	private int purchases;
	private double sum;

	Client(Scanner inp, boolean prompt) {
		if (prompt)
			System.out.print("Name: ");
		name = inp.nextLine();
		if (prompt)
			System.out.print("Registration date (dd.mm.yyyy): ");
		String s = inp.next();
		regDate = new Date(s);
		if (prompt)
			System.out.print("Purchases: ");
		purchases = inp.nextInt();
		if (prompt)
			System.out.print("Common sum: ");
		sum = inp.nextDouble();
		inp.nextLine();
	}

	public String getName() {
		return name;
	}

	public Date getRegDate() {
		return regDate;
	}

	public double getSum() {
		return sum;
	}

	public int getRating() {
		int p = 1;
		if (purchases >= 100)
			p++;
		if (purchases >= 300)
			p++;
		if (purchases >= 500)
			p++;
		if (purchases >= 1000)
			p++;
		return p;
	}

	public String ratingStars() {
		String s = "";
		int r = getRating();
		for (int i = 0; i < r; i++)
			s += '*';
		return s;
	}

	@Override
	public String toString() {
		return String.format("%s, %d, %.2f ", name, purchases, sum) + regDate + ", " + ratingStars();
	}

	@Override
	public int compareTo(Client c) {
		return this.regDate.compareTo(c.regDate);
	}
}

class Cmp1 implements Comparator<Client> {
	@Override
	public int compare(Client a, Client b) {
		return a.getName().compareTo(b.getName());
	}
}

class Cmp2 implements Comparator<Client> {
	@Override
	public int compare(Client a, Client b) {
		if (a.getSum() > b.getSum())
			return -1;
		if (a.getSum() < b.getSum())
			return 1;
		return a.getName().compareTo(b.getName());
	}
}

class PU2019_2 {
	static int N;
	static Scanner inp = null;
	static ArrayList<Client> clients = new ArrayList<Client>();

	static void problem1(boolean prompt) {
		for (int i = 0; i < N; i++) {
			if (prompt)
				System.out.printf("Client #%d data%n", i + 1);
			clients.add(new Client(inp, prompt));
		}
	}

	static void problem2() {
		clients.sort(new Cmp1());
		for (Client c : clients)
			System.out.println(c);
	}

	static void problem3(int r) {
		clients.sort(new Cmp2());
		for (Client c : clients)
			if (c.getRating() == r)
				System.out.println(c);
	}

	static void problem4(int r) {
		Collections.sort(clients);
		int y = clients.get(0).getRegDate().getYear();
		int s = 0;
		for (Client c : clients) {
			if (c.getRegDate().getYear() != y) {
				if (s > 0)
					System.out.println(y + " - " + s);
				y = c.getRegDate().getYear();
				s = (c.getRating() == r ? 1 : 0);
			} else
				s += (c.getRating() == r ? 1 : 0);
		}
		if (s > 0)
			System.out.println(y + " - " + s);
	}

	public static void main(String[] args) {
		final boolean prompt = false;
		File f = null;
		if (prompt) {
			inp = new Scanner(System.in);
			do {
				System.out.print("Client count (1 to 5000): ");
				N = inp.nextInt();
			} while (N < 1 || N > 5000);
		} else {
			f = new File("Data.txt");
			try {
				inp = new Scanner(f);
			} catch (Exception e) {
				System.out.println("File Data.txt not found");
				return;
			}
			N = inp.nextInt();
		}
		inp.nextLine();
		problem1(prompt);
		problem2();
		problem3(2);
		if (prompt)
			System.out.print("Rating for statictics (1 to 5): ");
		int r = inp.nextInt();
		inp.nextLine();
		inp.close();
		problem4(r);
	}
}
