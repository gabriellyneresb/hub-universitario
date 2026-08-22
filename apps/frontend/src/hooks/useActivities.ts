// apps/frontend/src/hooks/useActivities.ts

import { useMutation, useQuery } from "@tanstack/react-query";
import {
  createRegistration,
  getActivities,
  getActivity,
  getRegistrations,
} from "../services/activityService";
import type { RegistrationInput } from "../types/activity";

/**
 * Busca o catálogo de atividades, filtrado pelo termo de busca informado.
 *
 * O termo de busca compõe a `queryKey`, então o TanStack Query mantém um
 * cache separado por termo. Buscas diferentes não sobrescrevem o cache
 * umas das outras, e voltar a um termo já buscado reaproveita o cache.
 *
 * @param search termo de busca atual (string vazia lista todas as atividades)
 * @returns o resultado da query (`data`, `isLoading`, `isError`, etc.)
 */
export function useActivities(search: string) {
  return useQuery({
    queryKey: ["activities", search],
    queryFn: () => getActivities(search),
  });
}

export function useActivity(id: number) {
  return useQuery({
    queryKey: ["activities", id],
    queryFn: () => getActivity(id),
    enabled: Number.isFinite(id),
  });
}

export function useRegistrations(activityId: number) {
  return useQuery({
    queryKey: ["activities", activityId, "registrations"],
    queryFn: () => getRegistrations(activityId),
    enabled: Number.isFinite(activityId),
  });
}

export function useCreateRegistration(activityId: number) {
  return useMutation({
    mutationFn: (input: RegistrationInput) =>
      createRegistration(activityId, input),
  });
}
