public class MyFile {
    public int varCount=0;
    public int clauseCount=0;
    public int varCountV1=0;
    public int clauseCountV1=0;
    public int varCountV2=0;
    public int clauseCountV2=0;
    public int sol1=0;
    public int sol2=0;
    public float time1 = (float) 0.0; 
    public float time2 = (float) 0.0; 
    public boolean empty(){
        return varCount==0 &&
        clauseCount==0 &&
        varCountV1==0 || varCountV2==0 
        &&
        clauseCountV1==0 ||  clauseCountV2==0
        &&
        varCountV1==0 ||  varCountV2==0
        &&
        clauseCountV2==0 &&
        sol1==0 || sol2==0 
        &&
        time1==0 || time2==0
        ;
        
    }

    public void display(){
        if(time1!=time2&&time2!=0){
            // float x1   =   ((float)(int)(100*(float)varCountV1/(varCount)))/100;
            // float x2   =   ((float)(int)(100*(float)(clauseCountV1)/clauseCount))/100;
            // float x3   =   ((float)(int)(100*(float)(varCountV2)/varCount ))/100;
            // float x4   =   ((float)(int)(100*(float)(clauseCountV2)/clauseCount))/100;
            // System.out.println("inst & "+varCount+" & "+clauseCount+" & "+x1+" & "+x2+" & "+x3+" & "+x4+" \\\\");

            System.out.println( Math.log(1+sol1/time1*Math.log(clauseCountV1/clauseCount))
            +"," + Math.log(1+sol2/time2*Math.log(clauseCountV2/clauseCount)
            ));


        }
    }

}