package bspkrs.util;

public class Coord
{
    public int x;
    public int y;
    public int z;

    public Coord(int var1, int var2, int var3)
    {
        this.x = var1;
        this.y = var2;
        this.z = var3;
    }

    public Coord clone()
    {
        return new Coord(this.x, this.y, this.z);
    }

    public boolean equals(Object var1)
    {
        if (this == var1)
        {
            return true;
        }
        else if (!(var1 instanceof Coord))
        {
            return false;
        }
        else
        {
            Coord var2 = (Coord)var1;
            return this.x == var2.x && this.y == var2.y && this.z == var2.z;
        }
    }

    public Object clone() throws CloneNotSupportedException
    {
        return this.clone();
    }
}
