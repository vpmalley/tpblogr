package blogr.vpm.fr.blogr.insertion;

import android.content.Context;
import android.test.mock.MockContext;
import android.widget.EditText;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by vince on 07/01/15.
 */
@Config(emulateSdk = 18)
@RunWith(RobolectricTestRunner.class)
public class DefaultInserterTest {

  private Context mockContext;
  private EditText editText;

  @Before
  public void setUp() {
    //mockContext = Robolectric.newInstanceOf(MockContext.class);
    mockContext = Robolectric.getShadowApplication().getApplicationContext();
    editText = new EditText(mockContext);
    //Context mockContext = new MockContext();
    //mockContext = Mockito.mock(Context.class);
  }

  @Test
  public void testTwo() {
    Assertions.assertThat(2).isEqualTo(1 + 1);
  }

  @Test
  public void testThree() {
    Assertions.assertThat(3).isEqualTo(1+1);
  }

  @Test
  public void testSingleTagInsertion() {
    Inserter defaultInserter = new DefaultInserter(mockContext);
    editText.setText("Hello World");
    editText.setSelection(5);
    final String singleTag = "randomInsert";
    SingleTagProvider singleTagProvider = new SingleTagProvider() {
      @Override
      public String getTag() {
        return singleTag;
      }
    };

    defaultInserter.insert(editText, singleTagProvider);

    Assertions.assertThat(editText.getText().toString()).contains(singleTag);
    Assertions.assertThat(editText.getText().toString()).contains("Hello");
    Assertions.assertThat(editText.getText().toString()).contains("World");
    Assertions.assertThat(editText.getSelectionEnd()).isEqualTo(5 + singleTag.length());
  }

  @Test
  public void testDoubleTagInsertion() {
    MockContext mockContext = new MockContext();
    Inserter defaultInserter = new DefaultInserter(mockContext);
    EditText editText = new EditText(mockContext);
    editText.setText("Hello World");
    editText.setSelection(5);
    final String singleTag = "randomInsert";
    defaultInserter.insert(editText, new SingleTagProvider() {
      @Override
      public String getTag() {
        return singleTag;
      }
    });

    Assertions.assertThat(editText.getText().toString()).contains(singleTag);
    Assertions.assertThat(editText.getText().toString()).contains("Hello");
    Assertions.assertThat(editText.getText().toString()).contains("World");
    Assertions.assertThat(editText.getSelectionEnd()).isEqualTo(5 + singleTag.length());
  }


}
