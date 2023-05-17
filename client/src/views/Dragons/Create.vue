<template>
    <div class="container mt-5">
        <div class="card">
            <div class="card-header">
                <h4>Create Dragon</h4>
            </div>
            <div class="card-body">
                <ul class="alert alert-warning" v-if="Object.keys(this.errorList).length > 0">
                    <li class="m-b0 ms-3" v-for="(err, index) in this.errorList" :key="index">
                        {{ err }}
                    </li>
                </ul>
                <div class="mb-3">
                    <label for="">Name</label>
                    <input type="text" v-model="model.dragon.name" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">Description</label>
                    <input type="text" v-model="model.dragon.description" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">Family</label>
                    <input type="text" v-model="model.dragon.family" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">City Spotted</label>
                    <input type="text" v-model="model.dragon.spottedCity" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">Neighborhood Spotted</label>
                    <input type="text" v-model="model.dragon.spottedNeighborhood" class="form-control" />
                </div>
                <div class="mb-3">
                    <label for="">State Spotted</label>
                    <input type="text" v-model="model.dragon.spottedState" class="form-control" />
                </div>
                <div class="mb-3">
                    <button type="button" @click="saveDragon" class="btn btn-primary">Save</button>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import axios from 'axios'

export default {
    name: 'dragonsCreate',

    data() {
        return {
            errorList: [],

            model: {
                dragon: {
                    name: '',
                    description: '',
                    family: '',
                    spottedCity: '',
                    spottedNeighborhood: '',
                    spottedState: ''
                }
            }
        }
    },

    methods: {
        saveDragon() {
            this.errorList = [];
            console.log(this.model.dragon);

            for (const att in this.model.dragon) {
                if (this.model.dragon[att] === '') {
                    this.errorList.push('"' + att + '" is required');
                }
            }

            if (this.errorList.length == 0) {
                axios.post('API_URL/dragons', this.model.dragon)
                    .then(res => {
                        console.log(res)

                        this.model.dragon = {
                            name: '',
                            description: '',
                            family: '',
                            spottedCity: '',
                            spottedNeighborhood: '',
                            spottedState: ''
                        }
                    })
                    .catch(function (err) {
                        if (err.response) {
                            console.log(err.response.data)
                            console.log(err.response.status)
                            console.log(err.response.headers)
                        } else if (err.request) {
                            console.log(err.request)
                        } else {
                            console.log('Error', err.message)
                        }
                    });
            }
        }
    }
}
</script>
