<template>
    <div class="popup-container" :class="{ 'd-flex align-items-center justify-content-center': isOpen }">
        <div class="popup card" v-if="isOpen">
            <div ref="card" class="card-body">
                <div class="ratio ratio-21x9 img-thumbnail mb-2"></div>
                <h5 class="card-title">{{ card.name }}</h5>
                <p class="card-text">{{ card.description }}</p>
                <ul class="list-group">
                    <li class="list-group-item">
                        <strong>Family:</strong> {{ card.family }}
                    </li>
                    <li class="list-group-item">
                        <strong>City:</strong> {{ card.spottedCity }}
                    </li>
                    <li class="list-group-item">
                        <strong>Neighborhood:</strong> {{ card.spottedNeighborhood }}
                    </li>
                    <li class="list-group-item">
                        <strong>State:</strong> {{ card.spottedState }}
                    </li>
                </ul>
            </div>
            <div class="btn-group btn-group-sm mt-3">
                <button class="btn btn-success" @click="generateCardImage">
                    Download Card
                </button>
                <button class="btn btn-secondary" @click="closePopup">
                    Close
                </button>
            </div>
        </div>
    </div>
</template>

<script lang="ts">
import html2canvas from 'html2canvas'

export default {
    props: {
        card: {
            type: Object,
            required: true
        },

        isOpen: {
            type: Boolean,
            required: true
        }
    },

    data() {
        return {
            cardImage: ''
        }
    },

    methods: {
        closePopup() {
            this.$emit('closePopup');
        },

        generateCardImage() {
            html2canvas(this.$refs.card, { height: 510 }).then(canvas => {
                this.cardImage = canvas.toDataURL('image/png')
                console.log(this.cardImage)

                const link = document.createElement('a');
                link.href = this.cardImage;
                link.download = 'trading_card.png';
                link.click();
            });
        }
    }
}
</script>

<style>
.popup-container {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.5);
    z-index: 9999;
    display: none;
}

.popup {
    max-width: 400px;
    padding: 1rem;
    background-color: #fff;
    border-radius: 0.25rem;
    box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15);
}

.ratio {
    background-image: url('../../src/assets/dragon.png');
    background-size: contain;
    background-repeat: no-repeat;
    background-position: center;
}
</style>
