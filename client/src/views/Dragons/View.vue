<template>
    <div class="container my-5">
        <div class="card">
            <div class="card-header">
                <h4 class="mt-1">
                    Dragons
                    <router-link to="/dragons/create" class="btn btn-primary float-end">
                        Create Dragon
                    </router-link>
                </h4>
            </div>
            <div class="card-body">
                <div class="input-group mb-3">
                    <div class="input-group-prepend">
                        <button class="input-group-text dropdown-toggle" @click="switchSearchButton">{{ button }}</button>
                    </div>
                    <input type="text" v-model="search" class="form-control" placeholder="Search">
                    <div class="input-group-append">
                        <button class="btn btn-outline-secondary" @click="getDragons(true)" type="button">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-search mb-1" viewBox="0 0 16 16">
                                <path d="M11.742 10.344a6.5 6.5 0 1 0-1.397 1.398h-.001c.03.04.062.078.098.115l3.85 3.85a1 1 0 0 0 1.415-1.414l-3.85-3.85a1.007 1.007 0 0 0-.115-.1zM12 6.5a5.5 5.5 0 1 1-11 0 5.5 5.5 0 0 1 11 0z" />
                            </svg>
                        </button>
                        <button class="btn btn-outline-secondary ms-2" @click="getDragons()" type="button">
                            <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-bootstrap-reboot mb-1" viewBox="0 0 16 16">
                                <path d="M1.161 8a6.84 6.84 0 1 0 6.842-6.84.58.58 0 1 1 0-1.16 8 8 0 1 1-6.556 3.412l-.663-.577a.58.58 0 0 1 .227-.997l2.52-.69a.58.58 0 0 1 .728.633l-.332 2.592a.58.58 0 0 1-.956.364l-.643-.56A6.812 6.812 0 0 0 1.16 8z" />
                                <path d="M6.641 11.671V8.843h1.57l1.498 2.828h1.314L9.377 8.665c.897-.3 1.427-1.106 1.427-2.1 0-1.37-.943-2.246-2.456-2.246H5.5v7.352h1.141zm0-3.75V5.277h1.57c.881 0 1.416.499 1.416 1.32 0 .84-.504 1.324-1.386 1.324h-1.6z" />
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
                            <td class="btn-group btn-group-sm border-0">
                                <button type="button" @click="openPopup(dragon)" class="btn btn-success my-1">
                                    View
                                </button>
                                <router-link :to="{ path: '/dragons/edit/' + dragon.name }" class="btn btn-secondary my-1">
                                    Edit
                                </router-link>
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
        <Popup :card="selectedCard" :isOpen="isPopupOpen" @closePopup="closePopup" />
    </div>
</template>

<script lang="ts">
import axios from 'axios'
import { defineComponent } from 'vue'
import { Dragon, Search } from '../../types/DragonCards.interface'
import Popup from './Popup.vue'

export default defineComponent({
    name: 'dragonsView',

    data() {
        return {
            button: 'name',
            search: '',
            isPopupOpen: false,
            dragons: [] as Dragon[],
            selectedCard: {} as Dragon
        }
    },

    mounted() {
        this.getDragons(false);
    },

    methods: {
        getDragons(query: boolean): void {
            let params: Search = {};
            if (query) { params[this.button as keyof Search] = this.search; }

            axios.get('/dragons', { params: params })
                .then(res => {
                    console.log(res)
                    this.dragons = res.data
                })
                .catch(err => {
                    console.log(err)
                    if (this.$store.getters.token) {
                        this.$store.dispatch('expired')
                    }
                });
        },

        deleteDragon(dragonName: string): void {
            if (confirm('Are you sure you want to delete this entry for ' + dragonName + '?')) {
                console.log(dragonName);

                axios.delete('/dragons', { data: { name: dragonName } })
                    .then(res => {
                        console.log(res)
                        setTimeout(() => '', 1000)
                        this.getDragons(false)
                    })
                    .catch(err => {
                        console.log(err)
                        if (this.$store.getters.token) {
                            this.$store.dispatch('expired')
                        }
                    });
            }
        },

        switchSearchButton(): void {
            if (this.button === 'name') {
                this.button = 'family';
            }
            else {
                this.button = 'name';
            }
        },

        openPopup(dragon: Dragon): void {
            this.selectedCard = dragon;
            this.isPopupOpen = true;
        },

        closePopup(): void {
            this.isPopupOpen = false;
        }
    },

    components: {
        Popup
    }
});
</script>
