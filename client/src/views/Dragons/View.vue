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
                            <td class="align-middle">
                                <RouterLink :to="{ path: '/dragons/edit/' + dragon.name }" class="btn btn-success w-100 my-1">
                                    Edit
                                </RouterLink>
                                <button type="button" @click="deleteDragon(dragon.name)" class="btn btn-danger w-100 my-1">
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

export default {
    name: 'dragons',

    data() {
        return {
            dragons: []
        }
    },

    mounted() {
        this.getDragons();
    },

    methods: {
        getDragons(): void {
            // TODO replace mock API Endpoint
            axios.get('https://de5613ea-edb7-47c8-97b1-6e852c155697.mock.pstmn.io/dragons').then(res => {
                this.dragons = res.body
                console.log(this.dragons)
            });
        },

        deleteDragon(dragonName: string): void {
            if (confirm('Are you sure you want to delete this entry for ' + dragonName + '?')) {
                console.log(dragonName);
                let payload: { name: string; } = { name: dragonName };
                axios.delete('APIURL/dragons', payload)
                    .then(res => {
                        console.log(res)
                        this.getDragons()
                    })
                    .catch(function(err) {
                        if (err.response) {
                            console.log(err.response.data)
                            console.log(err.response.headers)
                        }
                        else if (err.request) {
                            console.log(err.request)
                        }
                        else {
                            console.log('Error', err.message)
                        }
                    });
            }
        }
    },
}
</script>
