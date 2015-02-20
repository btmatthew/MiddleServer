/*
 Copyright (c) 2010-2012 Nathan Rajlich
Permission is hereby granted, free of charge, to any person
obtaining a copy of this software and associated documentation
files (the "Software"), to deal in the Software without
restriction, including without limitation the rights to use,
copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the
Software is furnished to do so, subject to the following
conditions:

The above copyright notice and this permission notice shall be
included in all copies or substantial portions of the Software.
THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.
 */
//Copyright notice for websocket api used for purpose of this class
package old;
import org.java_websocket.client.*;
import org.java_websocket.handshake.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
/**
 *
 * @author SirRat
 */
public class WebSocket{
    
    protected WebSocketClient mWebSocketClient;
    protected byte[] data;
    public void connectWebSocket(){
        
    final URI uri;
    try {
        uri = new URI("ws://192.168.1.107:8080/MiddleServer/data");
    }catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }  
        mWebSocketClient = new WebSocketClient(uri){
        
            @Override
            public void onOpen(ServerHandshake serverHandshake) {//Open Socket
            }
            @Override
            public void onClose(int i, String s, boolean b) {//close Socket
            }
            @Override
            public void onError(Exception e) {//Error Socket
            }
            @Override
            public void onMessage(ByteBuffer x) {//message Socket
                
            }
            @Override
            public void onMessage(String x) {//message Socket
            }
    }; 
    mWebSocketClient.connect();
    }
    
}
