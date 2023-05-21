<template>
    <div class="container my-5">
        <div class="card">
            <div class="card-header">
                <h4 class="mt-1">
                    Dragons
                    <RouterLink to="/dragons/create" class="btn btn-primary float-end">
                        Create Dragon
                    </RouterLink>
                </h4>
            </div>
            <div class="card-body">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <button class="input-group-text dropdown-toggle" @click="switchSearchButton">{{ button }}</button>
                    </div>
                    <input type="text" v-model="model.search" class="form-control" placeholder="Search">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" @click="getDragons(true)" type="button">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search mb-1" viewBox="0 0 16 16">
                                <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z" />
                            </svg>
                        </button>
                        <button class="btn btn-outline-secondary ms-2" @click="getDragons()" type="button">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bootstrap-reboot" viewBox="0 0 16 16">
                                <path d="M1.161 8a6.84 6.84 0 1 0 6.842-6.84.58.58 0 1 1 0-1.16 8 8 0 1 1-6.556 3.412l-.663-.577a.58.58 0 0 1 .227-.997l2.52-.69a.58.58 0 0 1 .728.633l-.332 2.592a.58.58 0 0 1-.956.364l-.643-.56A6.812 6.812 0 0 0 1.16 8z"/>
                                <path d="M6.641 11.671V8.843h1.57l1.498 2.828h1.314L9.377 8.665c.897-.3 1.427-1.106 1.427-2.1 0-1.37-.943-2.246-2.456-2.246H5.5v7.352h1.141zm0-3.75V5.277h1.57c.881 0 1.416.499 1.416 1.32 0 .84-.504 1.324-1.386 1.324h-1.6z"/>
                            </svg>
                        </button>
                    </div>
                </div>
                <table class="table table-bordered">
                    <thead>
                        <tr>
                            <th>Name</th>
                            <th>Description</th>
                            <th>Family</th>
                            <th>City</th>
                            <th>Neighborhood</th>
                            <th>State</th>
                            <th>Action</th>
                        </tr>
                    </thead>
                    <tbody v-if="this.dragons.length > 0">
                        <tr v-for="(dragon, index) in this.dragons" :key="index">
                            <td>{{ dragon.name }}</td>
                            <td>{{ dragon.description }}</td>
                            <td>{{ dragon.family }}</td>
                            <td>{{ dragon.spottedCity }}</td>
                            <td>{{ dragon.spottedNeighborhood }}</td>
                            <td>{{ dragon.spottedState }}</td>
                            <td class="align-middle btn-group w-100">
                                <button type="button" @click="" class="btn btn-primary my-1">
                                    View
                                </button>
                                <RouterLink :to="{ path: '/dragons/edit/' + dragon.name }" class="btn btn-success my-1">
                                    Edit
                                </RouterLink>
                                <button type="button" @click="deleteDragon(dragon.name)" class="btn btn-danger my-1">
                                    Delete
                                </button>
                            </td>
                        </tr>
                    </tbody>
                    <tbody v-else>
                        <tr>
                            <td colspan="7">
                                Loading...
                            </td>
                        </tr>
                    </tbody>
                </table>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
    import axios from 'axios'

    interface Dragon {
        name: string;
        description: string;
        family: string;
        spottedCity: string;
        spottedNeighborhood: string;
        spottedState: string
    }

    export default {
        name: 'dragons',

        data() {
            return {
                dragons: Array < Dragon > ,
                button: 'name',

                model: {
                    search: ''
                }
            }
        },

        mounted() {
            this.getDragons(null);
        },

        methods: {
            getDragons(query: boolean): void {
                const pointer = this;
                let params = {}
                if (query) {
                    params[this.button] = this.model.search
                }

                axios.get('/dragons', { params: params })
                    .then(res => {
                        console.log(res)
                        this.dragons = res.data
                    })
                    .catch(function(err) {
                        console.log(err)
                        pointer.$store.dispatch('expired')
                    });
            },

            deleteDragon(dragonName: string): void {
                const pointer = this;
                if (confirm('Are you sure you want to delete this entry for ' + dragonName + '?')) {
                    console.log(dragonName);

                    axios.delete('/dragons', { data: { name: dragonName } })
                        .then(res => {
                            console.log(res)
                            setTimeout(null, 1000)
                            this.getDragons(null)
                        })
                        .catch(function(err) {
                            console.log(err)
                            pointer.$store.dispatch('expired')
                        });
                }
            },

            switchSearchButton(): void {
                if (this.button === 'name') {
                    this.button = 'family'
                }
                else {
                    this.button = 'name'
                }
            }
        },
    }
</script>
