package laba12;

public class Consumer implements Runnable{
	final Data data;
	boolean cheking = true;
	
	public Consumer(Data data) {
		this.data = data;
	}
	
	@Override
	public void run() {
		String s;
		int count, k;
		String l = " ,.?!;:()��\"-�";
		
		while (cheking) {
			s = data.getData();
			count = 0;
			k = 0;
			if (s == null) 
				System.out.println("\n����������� � �������� ������.");
			else {
				if (s.equals("exit"))
					cheking = false;
				else if (s.length() != 0){
					for (int i = 0; i < s.length(); i++) {
						if (l.contains(Character.toString(s.charAt(i)))){
							if (i - k > 1)
								count ++;
							k = i;
						} else if (i == s.length() - 1) {
							count ++;
						}
					}
					System.out.println("\n����������� ������� ������.\n������: "
							+ s + "\n����� ���� � ������: " + count);
				} else 
					System.out.println("\n����������� ������� ������ ������.");
			}
		}
		System.out.println("\n����������� �������� ������������ ������.");
	}
}
