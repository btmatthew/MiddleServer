package middleserver;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author SirRat
 */

import java.util.Random;


public final class TokenGenAdmin {

final String AB = "123admintoken123";
Random rnd = new Random();

    protected String randomString() 
    {
       int len =10;
       StringBuilder sb = new StringBuilder( len );
       for( int i = 0; i < len; i++ ) 
          sb.append( AB.charAt( rnd.nextInt(AB.length()) ) );
       return sb.toString();
    }/*
    protected String testAdminTokens(String token){
       int len =10;
       StringBuilder sb = new StringBuilder( len );
       for( int i = 0; i < len; i++ ) 
          sb.append( token.charAt( rnd.nextInt(token.length()) ) );
       return sb.toString();
    }*/
}

