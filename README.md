## HodgkinHuxleyNeuron

Project prepared as part of the university course "Numerical methods" in collaboration with [236535](https://github.com/236535).

## Table of contents
* [General info](#general-info)
* [Screenshots](#screenshots)
* [Technologies](#technologies)

## General info
This project is simulation of how action potentials in neurons are initiated and propagated basing on [Hodgkin–Huxley model](https://en.wikipedia.org/wiki/Hodgkin%E2%80%93Huxley_model). Electrical characteristics of neurons are approximated by set of nonlinear differential equations.

The application creates charts of 
* dependence of the gating parameters (m<sub>∞</sub>, n<sub>∞</sub> and h<sub>∞</sub>) depending on the voltage in the range from -50 to 150 mV,
* voltage u, ion currents (I<sub>Na</sub>, I<sub>K</sub> and I<sub>L</sub>) and values of the gating parameters (m, n and h) depending on time.

## Screenshots
![image](https://user-images.githubusercontent.com/44273512/121694273-4254f280-caca-11eb-9893-aef517b96667.png)

![image](https://user-images.githubusercontent.com/44273512/121695848-c065c900-cacb-11eb-9bb1-09e47211519a.png)

Additional use cases with a description in Polish can be found in the pdf file [report.pdf](https://github.com/IzabelaPopiel/HodgkinHuxleyNeuron/blob/master/report.pdf).

## Technologies
Project is created with:
* Java 11
* JavaFx 11.0.2
* Apache Commons Math 3.6.1
