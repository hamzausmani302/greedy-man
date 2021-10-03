package com.mygdx.game;




public class CollisonObject  {
    private int x;
    private int y;
    private int width;
    private int height;
    private int[] x_coord;
    private int[] y_coord;
    int type;
    public void settype(int n){
        type = n;
    }
    public int gettype(){
        return type;
    }
    public void setX(int x){
        this.x = x;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getWidth(){
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void setWidth(int w){
        this.width =  w;
    }
    public void setHeight(int y){
        this.height = y;
    }

    public CollisonObject(){
        x_coord = new int[4];
        y_coord = new int[4];
    }
    public   void make_rectangle(){
        x_coord[0] = x;
        y_coord[0] = y;
        x_coord[1] = x;
        y_coord[1] = y+height;
        x_coord[2] = x+width;
        y_coord[2] = y;
        x_coord[3] = x+width;
        y_coord[3] = y+width;
    }
    public void setY(int y){
        this.y = y;

    }

    public CollisonObject(int x , int y , int width , int height){
        x_coord = new int[4];
        y_coord = new int[4];
       this.x = x;
       this.y = y;
       this.width  = width;
       this.height = height;
    }
    public int check_collision(CollisonObject obj){


            if( (obj.y >= y &&  obj.y <= y+ height) || ((obj.y+obj.height) >= y && (obj.y + obj.height) <= (y+height) )   ){
                if( (obj.x >= x &&  obj.x <= x+ width) || ((obj.x+obj.width) >= x && (obj.x + obj.width) <= (x+width) )   ){
                    return 1;
                }
            }





        return 0;
    }


}
