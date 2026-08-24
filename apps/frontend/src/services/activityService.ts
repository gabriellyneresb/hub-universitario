// apps/frontend/src/services/activityService.ts

import { api } from "./api";
import type {
  Activity,
  Registration,
  RegistrationInput,
} from "../types/activity";

/**
 * Busca o catálogo de atividades, opcionalmente filtrado por um termo de busca.
 *
 * Quando `search` é uma string vazia, o parâmetro `search` não é enviado à API
 * e o backend retorna o catálogo completo. Quando `search` tem conteúdo, ele é
 * enviado como query param (`GET /activities?search=...`) e o backend filtra
 * por título ou descrição, sem diferenciar maiúsculas de minúsculas.
 *
 * @param search termo de busca digitado pelo usuário (padrão: string vazia)
 * @returns lista de atividades correspondentes à busca
 */
export async function getActivities(search = ""): Promise<Activity[]> {
  const response = await api.get<Activity[]>("/activities", {
    params: search ? { search } : undefined,
  });
  return response.data;
}

export async function getActivity(id: number): Promise<Activity> {
  const response = await api.get<Activity>(`/activities/${id}`);
  return response.data;
}

export async function getRegistrations(
  activityId: number,
): Promise<Registration[]> {
  const response = await api.get<Registration[]>(
    `/activities/${activityId}/registrations`,
  );
  return response.data;
}

export async function createRegistration(
  activityId: number,
  input: RegistrationInput,
): Promise<Registration> {
  const response = await api.post<Registration>(
    `/activities/${activityId}/registrations`,
    input,
  );
  return response.data;
}
