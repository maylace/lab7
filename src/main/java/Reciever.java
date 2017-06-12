import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.AudioFormat;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.sound.sampled.SourceDataLine;


public class Reciever  extends  Thread{
    private static final String IP_TO_STREAM_TO   = "localhost" ;
    private static final int PORT_TO_STREAM_TO     = 8888 ;

    /** Creates a new instance of RadioReceiver */
    public void run()
    {
        byte b[] = null ;
        while( true )
        {
            b = receiveThruUDP() ;
            toSpeaker( b ) ;
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Reciever r = new Reciever() ;
        r.run() ;

    }


    public static byte[] receiveThruUDP()
    {
        try
        {
            DatagramSocket sock = new DatagramSocket(PORT_TO_STREAM_TO) ;
            byte soundpacket[] = new byte[128000] ;
            DatagramPacket datagram = new DatagramPacket( soundpacket , soundpacket.length , InetAddress.getByName( IP_TO_STREAM_TO ) , PORT_TO_STREAM_TO ) ;
            sock.receive( datagram ) ;
            sock.close() ;
            return datagram.getData() ; // soundpacket ;
        }
        catch( Exception e )
        {
            System.out.println(" Unable to send soundpacket using UDP " ) ;
            return null ;
        }

    }


    public static void toSpeaker( byte soundbytes[] )
    {

        try{
            DataLine.Info dataLineInfo = new DataLine.Info( SourceDataLine.class , getAudioFormat() ) ;
            SourceDataLine sourceDataLine = (SourceDataLine)AudioSystem.getLine( dataLineInfo );
            sourceDataLine.open( getAudioFormat() ) ;
            sourceDataLine.start();
            int cnt = 0;
            sourceDataLine.write( soundbytes , 0, soundbytes.length );
            sourceDataLine.drain() ;
            sourceDataLine.close() ;
        }
        catch(Exception e )
        {
            System.out.println("not working in speakers " ) ;
        }

    }


    public static AudioFormat getAudioFormat()
    {
        float sampleRate = 44100.0F;
        //8000,11025,16000,22050,44100
        int sampleSizeInBits = 16;
        //8,16
        int channels = 2;
        //1,2
        boolean signed = true;
        //true,false
        boolean bigEndian = false;
        //true,false
        return new AudioFormat( sampleRate, sampleSizeInBits, channels, signed, bigEndian );
    }
}
