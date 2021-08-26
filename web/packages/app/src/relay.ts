import {getRelayEnvironment} from '@diekeditora/graphql';
import {http} from '@diekeditora/app';

export const relayEnvironment = getRelayEnvironment(http);
