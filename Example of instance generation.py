# -*- coding: utf-8 -*-
"""
Created on Wed Apr  7 14:20:44 2021

@author: lorenz75
"""

import random
import math
from decimal import *

""" This functions generates the Rendez-vous locations and the drone destinations as points in a square area"""
def locations(RVSet,gridSizeX,gridSizeY,n,m):

  Vp={} #define the set of rendez-vous locations  as a dict called Vp.
  Vd={} #define the set of drone destinations as a dict called Vd.
  
  """The following block of code generates the set of m  rendez-vous locations."""
  if(RVSet=='uniform'):
       for i in range(0,m):
          Vp[i]=[random.randint(0,gridSizeX*100)/100, random.randint(0,gridSizeY*100)/100] #randomly generate rv location coordinates
  if (RVSet=='grid'): # creates instances with rendez-vous locations being the intersection points of a regular grid over the square
      if m==4:
          Vp[0]=(1/3*gridSizeX,1/3*gridSizeY)
          Vp[1]=(1/3*gridSizeX,2/3*gridSizeY)
          Vp[1]=(2/3*gridSizeX,1/3*gridSizeY)
          Vp[1]=(2/3*gridSizeX,2/3*gridSizeY)
      if m==6:
          Vp[0]=(0,0)
          Vp[1]=(0,gridSizeX)
          Vp[2]=(2/3*gridSizeX,1/3*gridSizeY)
          Vp[3]=(1/3*gridSizeX,2/3*gridSizeY)
          Vp[4]=(0,gridSizeY)
          Vp[5]=(gridSizeX,gridSizeY)
      else:    
        k=0
        for i in range(int(math.sqrt(m))):
          for j in range(int(math.sqrt(m))):
            Vp[k]=(i/(math.sqrt(m)-1)*gridSizeX,j/(math.sqrt(m)-1)*gridSizeY) 
            k=k+1
          
  depot=Vp[0]
  Vd[0]=depot #depot is treated as extra destination, needed when finding a TSP, because depot needs to be included 
  """The following block of code generates the set of n drone destinations."""
  for i in range(1,n+1):
          Vd[i]=[random.randint(0,gridSizeX*100)/100, random.randint(0,gridSizeY*100)/100] #distribute destinations in a uniform fashion
        
  return Vp,Vd # return instance   



""" This function calculates the travel times between each pair of points for the drone (cd and cpd) and the truck  (cp) given their speeds alpha and beta"""
def traveltime(Vp,Vd,n,m, alpha,beta,RVSet):
  cd={} #declare the dest-dest travel times - symmetric
  cpd={} #declare the rv-destination travel times -  not symmetric! need to reverse!
  cp={} #declare rv-rv travel times -  symmetric
  for i in range(1,n+1):
      for j in range(1,n+1):
          myval=(Decimal.from_float((Vd[i][0]-Vd[j][0])**2 + (Vd[i][1]-Vd[j][1])**2 ).sqrt())/Decimal.from_float(alpha) ### the decimal transformation avoids rounding mistakes
          cd[(i,j)]= myval.quantize(Decimal('0.01'))  #pairwise travel times as euclidian distance/ drone speed
      for j in range(0,m):
          myval=(Decimal.from_float((Vd[i][0]-Vp[j][0])**2 + (Vd[i][1]-Vp[j][1])**2 ).sqrt())/Decimal.from_float(alpha) ### the decimal transformation avoids rounding mistakes
          cpd[(j,i)]= myval.quantize(Decimal('0.01'))  #pairwise travel times as euclidian distance/ drone speed      
  for i in range(0,m):
      for j in range(0,m):
          if RVSet=='grid':
              myval=(Decimal.from_float(abs(Vp[i][0]-Vp[j][0]) + abs(Vp[i][1]-Vp[j][1]) )/Decimal.from_float(beta)) ### the decimal transformation avoids rounding mistakes
              cp[(i,j)]= myval.quantize(Decimal('0.01'))  #pairwise travel times as manhatten distance/ truck speed 
          else:   
              myval=(Decimal.from_float((Vp[i][0]-Vp[j][0])**2 + (Vp[i][1]-Vp[j][1])**2 ).sqrt())/Decimal.from_float(beta) ### the decimal transformation avoids rounding mistakes
              cp[(i,j)]= myval.quantize(Decimal('0.01'))  #pairwise travel times as euclidian distance/ truck speed      
  return cd,cpd,cp            

#END Function to generate the travel time vectors