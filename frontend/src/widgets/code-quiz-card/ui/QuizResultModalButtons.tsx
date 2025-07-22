import { Button } from "antd";
import { useUserStore } from "@/shared/lib/store";

type Props = {
  onLike: () => void;
  onDislike: () => void;
  onNext: () => void;
};

export const QuizResultModalButtons = ({
  onLike,
  onDislike,
  onNext
}: Props) => {
  const { user } = useUserStore();

  return (
    <div className="flex justify-end gap-2 pt-2">
      {user ? (
        <>
          <Button
            onClick={onDislike}
            variant="outlined"
            color="danger"
          >
            Dislike
          </Button>
          <Button
            onClick={onLike}
            variant="outlined"
            color="primary"
          >
            Like
          </Button>
        </>
      ) : (
        <Button
          variant="outlined"
          color="primary"
          onClick={onNext}
        >
          Next
        </Button>
      )}
    </div>
  );
};
