import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.AudioFormat;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javax.sound.sampled.TargetDataLine;
import javax.sound.sampled.Port;

public class Microphone {
    private static final String IP_TO_STREAM_TO   = "localhost" ;
    private static final int PORT_TO_STREAM_TO     = 8888 ;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Mixer.Info minfo[] = AudioSystem.getMixerInfo() ;
        for( int i = 0 ; i < minfo.length ; i++ )
        {
            System.out.println( minfo[i] ) ;
        }


        if (AudioSystem.isLineSupported(Port.Info.MICROPHONE)) {
            try {
                DataLine.Info dataLineInfo = new DataLine.Info( TargetDataLine.class , getAudioFormat() ) ;
                TargetDataLine targetDataLine = (TargetDataLine)AudioSystem.getLine( dataLineInfo  ) ;
                targetDataLine.open( getAudioFormat() );
                targetDataLine.start();
                byte tempBuffer[] = new byte[64000] ;
                int cnt = 0 ;
                while( true )
                {
                    targetDataLine.read( tempBuffer , 0 , tempBuffer.length );
                    sendThruUDP( tempBuffer ) ;
                }

            }
            catch(Exception e )
            {
                System.out.println(" not correct " ) ;
                System.exit(0) ;
            }
        }
    }

    public static AudioFormat getAudioFormat(){
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


    public static void sendThruUDP( byte soundpacket[] )
    {
        try
        {
            DatagramSocket sock = new DatagramSocket() ;
            sock.send( new DatagramPacket( soundpacket , soundpacket.length , InetAddress.getByName( IP_TO_STREAM_TO ) , PORT_TO_STREAM_TO ) ) ;
            sock.close();
        }
        catch( Exception e )
        {
            e.printStackTrace() ;
            System.out.println(" Unable to send soundpacket using UDP " ) ;
        }

    }

}
