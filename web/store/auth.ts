import { AxiosResponse } from 'axios';

import { observable, flow, action, makeAutoObservable } from 'mobx';

import api from '~/api';

type User = {
  name: string;
  email: string;
  username: string;
  birthday: string;
  createdAt: string;
  updatedAt: string | null;
};

class AuthStore {
  @observable public loading: boolean = false;
  @observable public error: Error | null = null;
  @observable public token: string | null = null;
  @observable public user: User | null = null;

  constructor() {
    makeAutoObservable(this);
  }

  @action
  public login(token: string) {
    this.token = token;
  }

  @flow
  public *fetchLoggedUser() {
    this.loading = true;

    try {
      const response: AxiosResponse<User> = yield api.get('/session');

      this.user = response.data;
    } catch (error) {
      this.error = error;
    }

    this.loading = false;
  }
}

export default AuthStore;
