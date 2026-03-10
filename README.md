# LAB 8 — Threads, AsyncTask et Handler

---

## 1. Contexte

Cette application Android illustre comment exécuter des traitements longs sans bloquer le **UI Thread**. Chaque opération s'effectue dans un **Worker Thread** distinct, ce qui garde l'interface réactive en tout temps.

---

## 2. Interface

| Élément | Rôle |
|---|---|
| `TextView` | Affiche le statut de l'opération en cours |
| `ProgressBar` | Barre horizontale qui indique la progression (0 → 100) |
| `ImageView` | Zone d'affichage de l'image chargée |
| Bouton **Charger image** | Lance le chargement via un **Thread** |
| Bouton **Calcul lourd** | Déclenche un calcul via **AsyncTask** |
| Bouton **Afficher Toast** | Affiche un Toast immédiatement |

---

## 3. Fonctionnement

- **Thread** : charge une image en arrière-plan et utilise un **Handler** pour mettre à jour l'`ImageView` sur le **UI Thread** une fois le chargement terminé.
- **AsyncTask** : exécute un calcul intensif en tâche de fond. Les mises à jour de progression sont publiées via `onProgressUpdate`, ce qui fait avancer la **ProgressBar**.
- **Handler** : sert de pont entre le **Worker Thread** et le **UI Thread** pour modifier les éléments d'interface en toute sécurité.

---

## 4. Validation

Test effectué pour confirmer que le **UI Thread** n'est jamais bloqué :

1. Clic sur **Charger image** → chargement démarre en arrière-plan.
2. Clic immédiat sur **Afficher Toast** pendant le chargement.
3.  Le Toast apparaît instantanément → l'interface reste fluide.
4. Clic sur **Calcul lourd** → la **ProgressBar** progresse de 0 à 100 via **AsyncTask**.

---

## 5. Démonstration

## Vidéo de démonstration

![Détails](AppScreenshots/1.png)
![Détails](AppScreenshots/2.png)
![Détails](AppScreenshots/3.png)


