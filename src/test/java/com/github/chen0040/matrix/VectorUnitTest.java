package com.github.chen0040.matrix;


import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.within;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.testng.Assert.*;


/**
 * Created by xschen on 6/7/2017.
 */
public class VectorUnitTest {

   @Test
   public void testProjectOrthogonal(){
      Vector v1 = new Vector(10,new HashMap<Integer, Double>(){{
         put(1, 2.0);
         put(5, 2.0);
         put(7, 4.0);
      }});
      Vector v2  = new Vector(10, new HashMap<Integer, Double>(){{
         put(1, 1.0);
         put(4, 2.0);
      }});
      Vector v3 = v1.projectOrthogonal(Arrays.asList(v2));

      Vector v4 = v1.projectOrthogonal(Arrays.asList(v2, v3));

      assertThat(v4.multiply(v2)).isCloseTo(0.0, within(0.000001));
      assertThat(v4.multiply(v3)).isCloseTo(0.0, within(0.000001));

   }
}
