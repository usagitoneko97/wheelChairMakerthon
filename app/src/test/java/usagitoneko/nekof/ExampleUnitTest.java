package usagitoneko.nekof;

import android.content.Context;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ExampleUnitTest {
    @Rule
    public MockitoRule rule = MockitoJUnit.rule();
    @Mock
    Context mMockContext;


    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
        assertEquals(8, 2*4);
    }
}