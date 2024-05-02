# Description

This model describes a restaurant where clients arrive to a single queue and have the order taken by the dependientes in order of arrival. When an order is taken by a dependiente, it waits in the busy dependientes Queue until the cocinero is free and can start cooking the order. Once the order has been cooked the client pays for it and leaves. The clients leave the waiting queue when they have their order taken by the dependiente. The dependientes do not serve any other customer until payment has been made. The cooks exclusively prepare a customer's order and until they have finished with the entire order they do not start the next one.

It uses the Desmoj library to build the simulation and generate the reports.

# UML diagrams of the events:
## Generate client and Client arrived events
![genClient ClientArrivalEvent](https://github.com/Wousta/BurgerServiceSimulator/assets/66923315/290715d8-f2ae-4222-968b-2b8086e5c180)

## Order taken event
![comandaTomadaEv](https://github.com/Wousta/BurgerServiceSimulator/assets/66923315/5bc1feb1-13b6-4df1-9c3e-a50255e28eb0)

## Food ready event
![comidaCocinadaEv](https://github.com/Wousta/BurgerServiceSimulator/assets/66923315/edcc06c8-f07c-44e4-8c37-87eb18780576)

## Food paid event
![pedidoPagadoEv](https://github.com/Wousta/BurgerServiceSimulator/assets/66923315/43f25d9a-cc41-4c8f-8194-cf6f41a798fd)
