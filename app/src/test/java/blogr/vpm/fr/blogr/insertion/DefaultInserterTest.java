package blogr.vpm.fr.blogr.insertion;

import org.assertj.core.api.Assertions;

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
public class OtherInserterTest {

  @Test
  public void testTwo() {
    Assertions.assertThat(2).isEqualTo(1+1);
  }

  @Test
  public void testThree() {
    Assertions.assertThat(3).isEqualTo(1+1);
  }
}
