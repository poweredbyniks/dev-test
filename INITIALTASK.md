## Drones

[[_TOC_]]

---

:scroll: **START**


### Introduction

There is a major new technology that is destined to be a disruptive force in the field of transportation: **the droneEntity**. Just as the mobile phone allowed developing countries to leapfrog older technologies for personal communication, the droneEntity has the potential to leapfrog traditional transportation infrastructure.

Useful droneEntity functions include delivery of small items that are (urgently) needed in locations with difficult access.

---

### Task description

We have a fleet of **10 drones**. A droneEntity is capable of carrying devices, other than cameras, and capable of delivering small loads. For our use case **the load is medications**.

A **Drone** has:
- serial number (100 characters max);
- model (Lightweight, Middleweight, Cruiserweight, Heavyweight);
- weight limit (500gr max);
- battery capacity (percentage);
- state (IDLE, LOADING, LOADED, DELIVERING, DELIVERED, RETURNING).

Each **Medication** has:
- name (allowed only letters, numbers, ‘-‘, ‘_’);
- weight;
- code (allowed only upper case letters, underscore and numbers);
- image (picture of the medicationEntity case).

Develop a service via REST API that allows clients to communicate with the drones (i.e. **dispatch controller**). The specific communication with the droneEntity is outside the scope of this task.

The service should allow:
- registering a droneEntity;
- loading a droneEntity with medicationEntity items;
- checking loaded medicationEntity items for a given droneEntity;
- checking available drones for loading;
- check droneEntity battery level for a given droneEntity;

> Feel free to make assumptions for the design approach.

---

### Requirements

While implementing your solution **please take care of the following requirements**:

#### Functional requirements

- There is no need for UI;
- Prevent the droneEntity from being loaded with more weight that it can carry;
- Prevent the droneEntity from being in LOADING state if the battery level is **below 25%**;
- Introduce a periodic task to check drones battery levels and create history/audit event log for this.

---

#### Non-functional requirements

- Input/output data must be in JSON format;
- Your project must be buildable and runnable;
- Your project must have a README file with build/run/test instructions (use DB that can be run locally, e.g. in-memory, via container);
- Any data required by the application to run (e.g. reference tables, dummy data) must be preloaded in the database;
- Unit tests;
- Use a framework of your choice, but popular, up-to-date, and long-term support versions are recommended.

---

:scroll: **END**