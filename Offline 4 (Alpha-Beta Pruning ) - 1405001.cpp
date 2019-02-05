#include<bits/stdc++.h>
using namespace std;
#define MAX 3

#define FINISHED 0
#define RUNNING 1

#define DRAW 0
#define WIN 1
#define LOSE -1

struct Grid{
    int M[MAX][MAX];

    void Initialze() {memset(M,0,sizeof(M));}
    void SetFirst(int r,int c) {M[r][c]=1;}
    void SetSecond(int r,int c) {M[r][c]=-1;}
    pair<int,int> GetVal(){
        //ALL ROW
        for(int i=0;i<MAX;i++){
            int Sum=0;
            for(int j=0;j<MAX;j++) Sum+=M[i][j];
            if(Sum==MAX) return {FINISHED, WIN};
            if(Sum==-MAX) return {FINISHED, LOSE};
        }

        //ALL COLUMN
        for(int i=0;i<MAX;i++){
            int Sum=0;
            for(int j=0;j<MAX;j++) Sum+=M[j][i];
            if(Sum==MAX) return {FINISHED, WIN};
            if(Sum==-MAX) return {FINISHED, LOSE};
        }

        //FIRST DIAGONAL
        {
            int Sum=0;
            for(int i=0;i<MAX;i++) Sum+=M[i][i];
            if(Sum==MAX) return {FINISHED, WIN};
            if(Sum==-MAX) return {FINISHED, LOSE};
        }

        //SECOND DIAGONAL
        {
            int Sum=0;
            for(int i=0;i<MAX;i++) Sum+=M[i][MAX-1-i];
            if(Sum==MAX) return {FINISHED, WIN};
            if(Sum==-MAX) return {FINISHED, LOSE};
        }

        int Count=0;
        for(int i=0;i<MAX;i++) for(int j=0;j<MAX;j++) Count+=abs(M[i][j]);
        if(Count==MAX*MAX) return {FINISHED,DRAW};

        return {RUNNING,DRAW};
    }

    void Print(){
        for(int i=0;i<MAX;i++){
            for(int j=0;j<MAX;j++){
                if(M[i][j]==0) printf(".");
                if(M[i][j]==1) printf("x");
                if(M[i][j]==-1) printf("o");
            }
            printf("\n");
        }
        printf("\n\n");
    }
}G;

int MiniMax(Grid node,bool Player,int Alpha, int Beta){
    pair<int,int> vv = node.GetVal();
    if(vv.first==FINISHED) return vv.second;

    if(Player==0){
        int Max=INT_MIN;
        for(int i=0;i<MAX;i++){
            for(int j=0;j<MAX;j++){
                if(node.M[i][j]==0){

                    node.M[i][j]=1;
                    int val = MiniMax(node, !Player, Alpha, Beta);
                    node.M[i][j]=0;

                    Max=max(Max,val);
                    Alpha=max(Alpha, Max);
                    if(Beta<=Alpha) break;
                }
            }
            if(Beta<=Alpha) break;
        }
        return Max;
    }
    else{
        int Min=INT_MAX;
        for(int i=0;i<MAX;i++){
            for(int j=0;j<MAX;j++){
                if(node.M[i][j]==0){

                    node.M[i][j]=-1;
                    int val = MiniMax(node, !Player, Alpha, Beta);
                    node.M[i][j]=0;

                    Min=min(Min,val);
                    Beta=min(Beta, Min);
                    if(Beta<=Alpha) break;
                }
            }
            if(Beta<=Alpha) break;
        }
        return Min;
    }
}

int main(){
    G.Initialze();
    int Mode,r,c;
    printf("Mode 0 : Human vs Computer\n");
    printf("Mode 1 : Computer vs Computer\n");
    printf("Enter Mode : ");
    scanf("%d",&Mode);

    if(Mode==0){
        while(true){
            //HUMAN
            printf("Human's Move\n");
            printf("Enter row and column : ");
            scanf("%d %d",&r,&c);
            G.M[r][c]=-1;
            G.Print();
            if(G.GetVal().second==LOSE) {printf("Human Wins\n"); return 0;}

            //COMPUTER
            printf("Computer's Move\n");
            int WinX=-1,WinY=-1;
            int DrawX=-1,DrawY=-1;
            int LoseX=-1, LoseY=-1;
            int Ret=INT_MAX;

            for(int i=0;i<MAX;i++){
                for(int j=0;j<MAX;j++){
                    if(G.M[i][j]!=0) continue;

                    G.M[i][j]=1;
                    Ret=MiniMax(G, 1, INT_MIN, INT_MAX);
                    G.M[i][j]=0;

                    if(Ret==1) {WinX=i, WinY=j; break;}
                    else if(Ret==0) {DrawX=i; DrawY=j;}
                    else {LoseX=i; LoseY=j;}
                }
                if(Ret==1) break;
            }


            if(WinX!=-1) {printf("%d %d\n",WinX,WinY); G.M[WinX][WinY]=1;}
            else if(DrawX!=-1) {printf("%d %d\n",DrawX,DrawY); G.M[DrawX][DrawY]=1;}
            else if(LoseX!=-1) {printf("%d %d\n",LoseX,LoseY);  G.M[LoseX][LoseY]=1;}
            else {printf("DRAW !!!\n"); return 0;}

            G.Print();
            if(G.GetVal().second==WIN) {printf("Computer Wins\n"); return 0;}
        }
    }
    else{
        while(true){
            {
                //COMPUTER 1
                printf("Computer 1's Move\n");
                int WinX=-1,WinY=-1;
                int DrawX=-1,DrawY=-1;
                int LoseX=-1, LoseY=-1;
                int Ret=INT_MAX;

                for(int i=0;i<MAX;i++){
                    for(int j=0;j<MAX;j++){
                        if(G.M[i][j]!=0) continue;

                        G.M[i][j]=-1;
                        Ret=MiniMax(G, 0, INT_MIN, INT_MAX);
                        G.M[i][j]=0;

                        if(Ret==-1) {WinX=i, WinY=j; break;}
                        else if(Ret==0) {DrawX=i; DrawY=j;}
                        else {LoseX=i; LoseY=j;}
                    }
                    if(Ret==-1) break;
                }

                if(WinX!=-1) {printf("%d %d\n",WinX,WinY); G.M[WinX][WinY]=-1;}
                else if(DrawX!=-1) {printf("%d %d\n",DrawX,DrawY); G.M[DrawX][DrawY]=-1;}
                else if(LoseX!=-1) {printf("%d %d\n",LoseX,LoseY);  G.M[LoseX][LoseY]=-1;}
                else {printf("DRAW !!!\n"); return 0;}

                G.Print();
                if(G.GetVal().second==LOSE) {printf("Computer 1 Wins\n"); return 0;}
            }

            {
                //COMPUTER 2
                printf("Computer 2's Move\n");
                int WinX=-1,WinY=-1;
                int DrawX=-1,DrawY=-1;
                int LoseX=-1, LoseY=-1;
                int Ret=INT_MAX;

                for(int i=0;i<MAX;i++){
                    for(int j=0;j<MAX;j++){
                        if(G.M[i][j]!=0) continue;

                        G.M[i][j]=1;
                        Ret=MiniMax(G, 1, INT_MIN, INT_MAX);
                        G.M[i][j]=0;

                        if(Ret==1) {WinX=i, WinY=j; break;}
                        else if(Ret==0) {DrawX=i; DrawY=j;}
                        else {LoseX=i; LoseY=j;}
                    }
                    if(Ret==1) break;
                }


                if(WinX!=-1) {printf("%d %d\n",WinX,WinY); G.M[WinX][WinY]=1;}
                else if(DrawX!=-1) {printf("%d %d\n",DrawX,DrawY); G.M[DrawX][DrawY]=1;}
                else if(LoseX!=-1) {printf("%d %d\n",LoseX,LoseY);  G.M[LoseX][LoseY]=1;}
                else {printf("DRAW !!!\n"); return 0;}

                G.Print();
                if(G.GetVal().second==WIN) {printf("Computer Wins\n"); return 0;}
            }


        }
    }
}
