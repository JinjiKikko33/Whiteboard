

import java.util.Arrays;

public class point {
	private int a, b;
	private int[] array = new int[2];
	
	public point(int a, int b){
		array = new int[2];
		this.a = a;
		this.b = b;
		array[0] = a;
		array[1] = b;
	}
	
	public int[] getArray() {
		return array;
	}
	
	public int getX() {
		return a;
	}
	
	public int getY() {
		return b;
	}
	
	@Override
	public int hashCode() {
		return (31 * (31 * Arrays.hashCode(array) + a)) + b;
	}
	
	@Override
    public boolean equals(Object o) {
        if (o instanceof point) {
            point m = (point)o;
            return m.a == a && m.b == b && Arrays.equals(m.array, array);
        }
        else 
            return false;
   }
}
