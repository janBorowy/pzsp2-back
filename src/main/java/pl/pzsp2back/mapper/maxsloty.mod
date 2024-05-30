

set Pracownicy;
set T;
set OfertyWant within Pracownicy;

param v0 {Pracownicy,T} binary;
param WantDown{i in OfertyWant, t in T} binary, <= v0[i,t];                      #sprawdzenie, czy 1 gdy v0 == 1
param CanDown{i in Pracownicy, t in T} binary,  <= v0[i,t];                      #j.w. plus potrzebne bedzie sprawdzenie, czy 1 gdy WantDown == 1
param CanUp{i in Pracownicy, t in T} binary, <= 1-v0[i,t]  ;                     #sprawdzenie, czy 1 gdy v0 == 0;
      #do tego  sprawdzenie czy dla kazdego WantDown jest jakies niezerowe CanUp
check: sum{i in OfertyWant, t in T} CanUp[i,t] >= sum{i in OfertyWant, t in T} WantDown[i,t];

param cenaWant{OfertyWant} >= 0;                                                #cena dla oferty powiazanej - jej przyjecie oznacza, ze pracownik dostanie wolne
param cenaCan{i in Pracownicy, t in T} default 0;

param minLSlotow{Pracownicy} >= 0;

var d{OfertyWant}>= 0,<=1;                                                      #czy przyjeta oferta typu 'want'
var vUp{Pracownicy,t in T} >= 0,<=1;                                            #czy zmienil stan na 'praca'
var vDown{Pracownicy,t in T} >= 0,<=1;                                          #czy zmienil stan na 'wolne'

maximize Zmax: sum{i in OfertyWant}cenaWant[i]*d[i] - sum{t in T, i in Pracownicy: i not in OfertyWant} cenaCan[i,t]*( vUp[i,t]+vDown[i,t]) ;

subject to ogrJesliOfertaWantPrzyjetaToWolne {i in OfertyWant, t in T: WantDown[i,t]==1}:
    vDown[i,t] = d[i] ;

subject to ogrJesliBrakOfertyBrakZmianDown {i in Pracownicy, t in T}:
    vDown[i,t] <= CanDown[i,t];

subject to ogrJesliBrakOfertyBrakZmianUp {i in Pracownicy, t in T}:
    vUp[i,t]   <= CanUp[i,t] ;

subject to ogrMinLiczbaSlotow {i in Pracownicy}:
    sum {t in T} ( v0[i,t]+vUp[i,t]*CanUp[i,t]-vDown[i,t]*CanDown[i,t] ) == minLSlotow[i];

subject to ogrBilans {t in T}:
   sum {i in Pracownicy} vDown[i,t] == sum {i in Pracownicy} vUp[i,t]*CanUp[i,t];