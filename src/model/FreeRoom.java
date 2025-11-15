package model;

public class FreeRoom extends Room{
    public FreeRoom(String roomNumber,RoomType enumeration){
        super(roomNumber,0.0,enumeration);
    }
    @Override
    public String toString(){
        return "Free Room Number: "+getRoomNumber()+" Type: "+getRoomType()+" Price: $0.0";
    }
}
