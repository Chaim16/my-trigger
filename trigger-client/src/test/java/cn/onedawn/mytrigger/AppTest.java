package cn.onedawn.mytrigger;

import static org.junit.Assert.assertTrue;

import cn.onedawn.mytrigger.call.HTTPCallListener;
import org.junit.Test;

import java.io.IOException;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    /**
     * Rigorous Test :-)
     */
    @Test
    public void shouldAnswerWithTrue()
    {
        assertTrue( true );
    }

    @Test
    public void httpCallTest() throws IOException {
        HTTPCallListener httpCallListener = new HTTPCallListener();
        httpCallListener.init();
        while (true) {

        }
    }
}
