export interface Dragon {
  name: string
  description: string
  family: string
  spottedCity: string
  spottedNeighborhood: string
  spottedState: string
};

export interface EditDragon extends Dragon {
  oldName: string
};

export interface userData {
  username: string
  token: string
};

export interface State extends userData {
  sessionExpired: boolean
};

export interface loginForm {
  username: string
  password: string
};

export interface Search {
  name?: string
  family?: string
}
