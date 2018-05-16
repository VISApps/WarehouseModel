package org.visapps.warehousemodel.models;

import android.util.Log;

import org.visapps.warehousemodel.WarehouseModel;
import org.visapps.warehousemodel.utils.FewCustomersException;
import org.visapps.warehousemodel.utils.FewProductsException;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Modeling {

    private Random random;

    private List<String> names;

    private int ccount;
    private int pcount;

    private int A1;
    private int[] b;
    private int[] R;
    private int[] D;
    private int[] l;
    private int S;
    private int G;
    private int[] F;
    private double[] K;
    private int H;
    private double L;


    public Modeling(){
        random = new Random(System.currentTimeMillis());
    }

    public void init(List<Product> products, List<Customer> customers, int G) throws Exception
    {
        if(products.size() < 3){
            throw new FewProductsException();
        }
        if(customers.size() < 3){
            throw new FewCustomersException();
        }
        this.G = G;
        pcount = products.size();
        ccount = customers.size();
        names = new ArrayList<>();
        for(int i=0; i<pcount; i++){
            names.add(products.get(i).getName());
        }
        b = new int[pcount];
        for(int i=0; i<pcount; i++){
            b[i] = Integer.parseInt(products.get(i).getCurrentquantity());
        }
        R = new int[pcount];
        for(int i=0; i<pcount; i++){
            R[i] = Integer.parseInt(products.get(i).getOrderquantity());
        }
        D = new int[pcount];
        l = new int[pcount];
        for(int i=0; i<pcount; i++){
            l[i] = Integer.parseInt(products.get(i).getMinimalquantity());
        }
        F = new int[pcount];
        K = new double[pcount];
        S = 0;
    }

    public Result getResult(){
        while(S < G){
            performmodeling(F,D,A1,l,R,b);
            S = S + 1;
        }
        for(int i=0 ; i< K.length; i++){
            K[i] = Math.round(((double) D[i] / (double) F[i]) * 100.0 * 100.0) / 100.0;
        }
        for(int i=0 ; i< D.length; i++){
            H = H + D[i];
        }
        L = Math.round(((double) H / (double) A1) * 100.0 * 100.0) / 100.0;
        List<ResultItem> items = new ArrayList<>();
        for(int i=0; i<K.length; i++){
            items.add(new ResultItem(names.get(i),K[i]));
        }
        return new Result(G,System.currentTimeMillis(),L, items);
    }



    private void performmodeling(int[] F, int[] D, int A2, int[] l, int[] R, int[] b){
        int[] C = new int[pcount];
        int[][] a = new int[ccount][pcount];
        int[] summj = new int[pcount];
        for(int i=0 ; i<ccount; i++){
            int psize = random.nextInt(10) + 1;
            int[] p = new int[psize];
            for(int j=0; j<psize; j++){
                p[j] = random.nextInt(pcount);
            }
            for(int j=0; j<pcount; j++){
                for(int k=0 ; k<p.length; k++){
                    if(p[k] == j){
                        a[i][j] = a[i][j]+1;
                    }
                }
            }
        }
        for(int i=0 ; i<ccount; i++){
            for(int j=0; j<pcount; j++){
                summj[j]= a[i][j]+summj[j];
            }
        }
        int A = 0;
        for(int i=0 ; i<summj.length; i++){
            A = A + summj[i];
        }
        for(int i=0 ; i<this.F.length; i++){
            this.F[i] = F[i] + summj[i];
        }
        for(int i=0 ; i<summj.length; i++){
            C[i] = b[i] - summj[i];
        }
        for(int i=0 ; i<C.length; i++){
            if(C[i]<0){
                this.b[i] = R[i];
                this.D[i] = D[i] + Math.abs(C[i]);
            }
            else if(C[i] < l[i] && C[i] >= 0){
                b[i] = C[i] + R[i];
            }
            else{
                b[i] = C[i];
            }
        }
        A1 = A2 + A;
    }
}
